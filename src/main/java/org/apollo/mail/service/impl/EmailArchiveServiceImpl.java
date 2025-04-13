package org.apollo.mail.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apollo.mail.entity.ArchiveEmail;
import org.apollo.mail.entity.MailboxConfig;
import org.apollo.mail.repository.ArchiveEmailRepository;
import org.apollo.mail.service.EmailArchiveService;
import org.apollo.mail.service.MailboxConfigService;
import org.apollo.mail.util.MailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Service
public class EmailArchiveServiceImpl implements EmailArchiveService {

    @Autowired
    private MailboxConfigService mailboxConfigService;

    @Autowired
    private ArchiveEmailRepository archiveEmailRepository;

    @Value("${mail.attachment.path}")
    private String attachmentPath;

    @Value("${mail.batch.size:50}")
    private int batchSize;

    @Override
    @Async
    public void archiveEmails(Long mailboxConfigId) {
        try {
            processMailbox(mailboxConfigId);
        } catch (Exception e) {
            log.error("Failed to archive emails for mailbox {}", mailboxConfigId, e);
        }
    }

    @Override
    public Page<ArchiveEmail> searchEmails(String fromEmail, Date startTime, Date endTime, Pageable pageable) {
        // Convert Date to LocalDateTime
        LocalDateTime startLdt = (startTime != null) ? LocalDateTime.ofInstant(startTime.toInstant(), ZoneId.systemDefault()) : null;
        LocalDateTime endLdt = (endTime != null) ? LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()) : null;

        // Call the new repository method
        return archiveEmailRepository.searchEmails(
                fromEmail,
                startLdt,
                endLdt,
                pageable
        );
    }

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    public void processMailbox(Long mailboxConfigId) {
        MailboxConfig config = mailboxConfigService.getConfig(mailboxConfigId);
        if (!config.getActive()) {
            log.info("Mailbox {} is not active, skipping", mailboxConfigId);
            return;
        }

        Store store = null;
        Folder folder = null;
        try {
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", config.getProtocol());
            
            Session session = Session.getInstance(props);
            store = session.getStore();
            store.connect(config.getHost(), config.getPort(), config.getUsername(), config.getPassword());

            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);

            int messageCount = folder.getMessageCount();
            log.info("Found {} messages in mailbox {}", messageCount, mailboxConfigId);

            int startIndex = Math.max(1, messageCount - batchSize + 1);
            Message[] messages = folder.getMessages(startIndex, messageCount);

            for (Message message : messages) {
                try {
                    processMessage((MimeMessage) message, config);
                } catch (Exception e) {
                    log.error("Failed to process message", e);
                }
            }

        } catch (Exception e) {
            log.error("Failed to process mailbox {}", mailboxConfigId, e);
            throw new RuntimeException("Failed to process mailbox", e);
        } finally {
            try {
                if (folder != null && folder.isOpen()) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                log.error("Failed to close connections", e);
            }
        }
    }

    private void processMessage(MimeMessage message, MailboxConfig config) throws Exception {
        String uid = String.valueOf(message.getMessageNumber());
        if (archiveEmailRepository.findByUid(uid).isPresent()) {
            log.debug("Message {} already archived, skipping", uid);
            return;
        }

        ArchiveEmail archiveEmail = new ArchiveEmail();
        archiveEmail.setUid(uid);
        archiveEmail.setFromEmail(MailHelper.getFromAddr(message));
        archiveEmail.setSubject(message.getSubject());
        archiveEmail.setBody(MailHelper.getMailContent(message));
        
        Date receivedDate = message.getReceivedDate();
        if (receivedDate == null) {
            receivedDate = new Date();
        }
        archiveEmail.setReceiveTime(LocalDateTime.ofInstant(receivedDate.toInstant(), ZoneId.systemDefault()));
        archiveEmail.setMailboxConfig(config);

        boolean hasAttachment = MailHelper.isContainAttachment(message);
        archiveEmail.setHasAttachment(hasAttachment);

        if (hasAttachment) {
            StringBuilder attachmentPaths = new StringBuilder();
            MailHelper.saveAttachment(message, attachmentPath, attachmentPaths, archiveEmail.getSubject());
            archiveEmail.setAttachmentFilePaths(attachmentPaths.toString());
        }

        archiveEmailRepository.save(archiveEmail);
        log.info("Archived email: {}", archiveEmail.getSubject());
    }
} 