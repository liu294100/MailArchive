package org.apollo.mail.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.apollo.mail.entity.MailboxConfig;
import org.apollo.mail.service.EmailArchiveService;
import org.apollo.mail.service.MailboxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EmailArchiveScheduler {

    @Autowired
    private EmailArchiveService emailArchiveService;

    @Autowired
    private MailboxConfigService mailboxConfigService;

    @Scheduled(cron = "${mail.archive.cron:0 0/30 * * * ?}")  // Default: every 30 minutes
    public void scheduleEmailArchiving() {
        log.info("Starting scheduled email archiving task");
        List<MailboxConfig> activeConfigs = mailboxConfigService.getActiveConfigs();
        
        for (MailboxConfig config : activeConfigs) {
            try {
                emailArchiveService.archiveEmails(config.getId());
            } catch (Exception e) {
                log.error("Failed to archive emails for mailbox {}", config.getId(), e);
            }
        }
    }
} 