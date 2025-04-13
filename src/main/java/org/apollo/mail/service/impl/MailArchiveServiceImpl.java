package org.apollo.mail.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apollo.mail.entity.MailArchive;
import org.apollo.mail.entity.MailConfig;
import org.apollo.mail.entity.MailIdsCheck;
import org.apollo.mail.mapper.MailArchiveMapper;
import org.apollo.mail.service.MailArchiveService;
import org.apollo.mail.service.MailConfigService;
import org.apollo.mail.service.MailIdsCheckService;
import org.apollo.mail.service.MailUidService;
import org.apollo.mail.util.MailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import com.sun.mail.pop3.POP3Folder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MailArchiveServiceImpl implements MailArchiveService {

    @Autowired
    private MailConfigService mailConfigService;

    @Value("${mail.tmp.base-path}")
    private String fileBasePath;
    
    @Value("${mail.archive.root-path:archives}")
    private String archiveRootPath;
    
    @Value("${mail.archive.content-filename:MailContext.html}")
    private String contentFileName;

    private static final String EMAIL_CTX_FILE_NAME = "EMAIL_CTX.html";
    private static final Pattern FILE_PATTERN = Pattern.compile("^.*\\.(xls|xlsx|pdf|zip|rar|csv)");

    @Autowired
    private MailUidService mailUidService;
    
    @Autowired
    private MailIdsCheckService mailIdsCheckService;
    
    @Autowired
    private MailArchiveMapper mailArchiveMapper;

    @Override
    public List<Message> getArchiveMessages(Folder folder, String mailType) throws Exception {
        List<Message> messages = new ArrayList<>();
        
        // 获取最新的50条邮件
        int count = folder.getMessageCount() > 49 ? folder.getMessageCount() - 49 : 1;
        Message[] latestMsgs = folder.getMessages(count, folder.getMessageCount());
        
        // 获取需要检查的邮件
        Message[] needCheckMsgs = getMessagesForCheck(folder, latestMsgs, mailType);
        
        // 获取上一轮处理的邮件ID列表
        String processedIds = mailIdsCheckService.getMailIds(mailType);
        Set<String> processedUids = new HashSet<>();
        
        // 将邮件ID字符串转换为集合
        if (StringUtils.isNotBlank(processedIds)) {
            String[] ids = processedIds.split(",");
            for (String id : ids) {
                processedUids.add(id.trim());
            }
        }
        
        // 记录邮件主题用于日志
        StringBuffer mailSubject = new StringBuffer();
        
        // 找出未处理的邮件
        for (Message message : needCheckMsgs) {
            String uid = ((POP3Folder) folder).getUID(message);
            if (!processedUids.contains(uid)) {
                messages.add(message);
                mailSubject.append(message.getSubject()).append("\n");
            }
        }
        
        log.info("邮箱：{} 邮箱最近{}条内未解析的邮件个数：{}", mailType, needCheckMsgs.length, messages.size());
        log.info("分别为：\n{}", mailSubject);
        
        return messages;
    }

    /**
     * 保存邮件内容和附件
     */
    @Override
    public void saveMailContent(Message message, String mailType) throws Exception {
        String uid = ((POP3Folder) message.getFolder()).getUID(message);
        Date sentDate = message.getSentDate();
        String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(sentDate);
        String yyyy = yyyyMMdd.substring(0, 4);
        String MM = yyyyMMdd.substring(4, 6);
        String dd = yyyyMMdd.substring(6, 8);
        
        // 构建保存路径 - 按年/月/日/邮件ID归档
        String archivePath = archiveRootPath + File.separator + mailType + 
                             File.separator + yyyy + File.separator + MM + 
                             File.separator + dd + File.separator + uid + File.separator;
        
        // 创建目录
        File archiveDir = new File(archivePath);
        if (!archiveDir.exists()) {
            archiveDir.mkdirs();
        }
        
        // 保存邮件到数据库
        MailArchive mailArchive = saveMailToDatabase(message, mailType, archivePath, yyyyMMdd, dd);
        
        // 保存邮件正文
        if (message instanceof MimeMessage) {
            MimeMessage mimeMessage = (MimeMessage) message;
            String mailContent = getMailContent(mimeMessage);
            if (StringUtils.isNotBlank(mailContent)) {
                saveFileContent(mailContent, archivePath + contentFileName);
            }
        }
        
        // 保存附件
        boolean hasAttachment = saveAttachments(message, archivePath);
        
        // 更新附件信息
        if (hasAttachment && mailArchive != null) {
            mailArchive.setHasAttachment("1");
            mailArchiveMapper.updateById(mailArchive);
        }
        
        // 添加到已处理集合
        mailIdsCheckService.addMailId(mailType, uid);
        
        log.info("邮件归档完成：{}, 保存路径: {}", message.getSubject(), archivePath);
    }

    /**
     * 获取邮件正文内容
     */
    private String getMailContent(MimeMessage message) throws Exception {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain") || bodyPart.isMimeType("text/html")) {
                    return (String) bodyPart.getContent();
                }
            }
        }
        return null;
    }

    /**
     * 保存附件
     * @return 是否有附件
     */
    private boolean saveAttachments(Message message, String filepath) throws Exception {
        boolean hasAttachment = false;
        if (message instanceof MimeMessage) {
            MimeMessage mimeMessage = (MimeMessage) message;
            Object content = mimeMessage.getContent();
            if (content instanceof Multipart) {
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    String fileName = bodyPart.getFileName();
                    if (fileName != null && FILE_PATTERN.matcher(fileName).matches()) {
                        // 处理文件名，避免非法字符
                        fileName = fileName.replaceAll("[\\\\/:*?\"<>|]", "_");
                        String filePath = filepath + fileName;
                        try (InputStream is = bodyPart.getInputStream();
                             FileOutputStream fos = new FileOutputStream(filePath)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, bytesRead);
                            }
                            hasAttachment = true;
                            log.info("保存附件：{}", fileName);
                        }
                    }
                }
            }
        }
        return hasAttachment;
    }

    /**
     * 保存文件内容
     */
    private void saveFileContent(String content, String filepath) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(filepath), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
    
    /**
     * 保存邮件到数据库
     */
    private MailArchive saveMailToDatabase(Message message, String mailType, String archivePath, String yyyyMMdd, String dd) throws Exception {
        try {
            MailArchive mailArchive = new MailArchive();
            
            // 设置基本信息
            String uid = ((POP3Folder) message.getFolder()).getUID(message);
            mailArchive.setMailUid(uid);
            mailArchive.setMailType(mailType);
            mailArchive.setSubject(message.getSubject());
            mailArchive.setHasAttachment("0"); // 默认无附件，后续检测到附件后更新
            StringBuilder attachmentPaths = new StringBuilder();
            
            // 设置时间信息
            Date sentDate = message.getSentDate();
            Date receiveDate = new Date(); // 当前时间作为接收时间
            mailArchive.setSendDate(sentDate);
            mailArchive.setReceiveDate(receiveDate);
            mailArchive.setUpdateTime(receiveDate);
            
            // 设置年月日信息用于查询
            String receiveYm = yyyyMMdd.substring(0, 6); // 年月
            mailArchive.setReceiveYm(receiveYm);
            mailArchive.setReceiveDay(dd);
            
            // 设置小时信息
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            mailArchive.setReceiveHour(hourFormat.format(receiveDate));
            
            // 设置发件人和收件人信息
            if (message instanceof MimeMessage) {
                MimeMessage mimeMessage = (MimeMessage) message;
                try {
                    // 获取发件人
                    if (mimeMessage.getFrom() != null && mimeMessage.getFrom().length > 0) {
                        mailArchive.setSender(mimeMessage.getFrom()[0].toString());
                    }
                    
                    // 获取收件人
                    if (mimeMessage.getRecipients(Message.RecipientType.TO) != null) {
                        StringBuilder recipients = new StringBuilder();
                        for (javax.mail.Address address : mimeMessage.getRecipients(Message.RecipientType.TO)) {
                            recipients.append(address.toString()).append(";");
                        }
                        mailArchive.setRecipient(recipients.toString());
                    }
                    
                    // 获取抄送人
                    if (mimeMessage.getRecipients(Message.RecipientType.CC) != null) {
                        StringBuilder cc = new StringBuilder();
                        for (javax.mail.Address address : mimeMessage.getRecipients(Message.RecipientType.CC)) {
                            cc.append(address.toString()).append(";");
                        }
                        mailArchive.setCc(cc.toString());
                    }
                } catch (Exception e) {
                    log.error("获取邮件地址信息失败: {}", e.getMessage());
                }
            }
            
            // 保存附件
            boolean hasAttachment = MailHelper.saveAttachment(message, archivePath, attachmentPaths, mailArchive.getSubject());
            
            // 更新附件信息
            if (hasAttachment) {
                mailArchive.setHasAttachment("1");
            }
            
            // 保存到数据库
            mailArchive.setAttachmentPath(attachmentPaths.toString());
            mailArchiveMapper.insert(mailArchive);
            log.info("邮件信息已保存到数据库, ID: {}", mailArchive.getId());
            
            return mailArchive;
        } catch (Exception e) {
            log.error("保存邮件到数据库失败: {}", e.getMessage(), e);
            return null;
        }
    }
    
    private Message[] getMessagesForCheck(Folder folder, Message[] messages, String mailType) throws Exception {
        // 获取上一轮处理的邮件ID列表
        String processedIds = mailIdsCheckService.getMailIds(mailType);
        Set<String> processedUids = new HashSet<>();
        
        // 将邮件ID字符串转换为集合
        if (StringUtils.isNotBlank(processedIds)) {
            String[] ids = processedIds.split(",");
            for (String id : ids) {
                processedUids.add(id.trim());
            }
        }
        
        // 如果没有处理记录，直接返回当前批次
        if (processedUids.isEmpty()) {
            return messages;
        }
        
        // 检查当前批次是否有交集
        boolean hasIntersection = false;
        for (Message message : messages) {
            String uid = ((POP3Folder) folder).getUID(message);
            if (processedUids.contains(uid)) {
                hasIntersection = true;
                break;
            }
        }
        
        // 如果当前批次没有交集，递增查询范围：50 -> 100 -> 200 -> 400 -> ...
        if (!hasIntersection) {
            int totalCount = folder.getMessageCount();
            int checkCount = 50; // 初始检查数量为50
            
            while (!hasIntersection && checkCount < totalCount) {
                // 翻倍查询范围
                checkCount = checkCount * 2;
                
                // 确保startIndex大于0
                int startIndex = Math.max(1, totalCount - checkCount + 1);
                
                log.info("未找到邮件交集，增加查询范围至{}封邮件", checkCount);
                Message[] expandedMessages = folder.getMessages(startIndex, totalCount);
                
                // 检查扩大范围后的批次是否有交集
                for (Message message : expandedMessages) {
                    String uid = ((POP3Folder) folder).getUID(message);
                    if (processedUids.contains(uid)) {
                        hasIntersection = true;
                        log.info("在扩大范围至{}封邮件后找到交集", checkCount);
                        return expandedMessages;
                    }
                }
                
                // 如果查询范围已经覆盖所有邮件，则退出循环
                if (checkCount >= totalCount) {
                    log.info("已查询所有{}封邮件，仍未找到交集", totalCount);
                    return expandedMessages;
                }
            }
            
            // 如果执行到这里，说明在循环中找到了交集或已查询所有邮件
            // 返回最后一次查询的结果
            int startIndex = Math.max(1, totalCount - checkCount + 1);
            return folder.getMessages(startIndex, totalCount);
        }
        
        return messages;
    }
    
    /**
     * 执行邮件归档任务
     * 每10分钟执行一次
     */
    @Override
    @Scheduled(cron = "0 */10 * * * ?") 
    public void executeMailArchiveTask() {
        log.info("开始执行邮件归档任务...");
        
        try {
            // 获取启用的邮箱配置
            MailConfig mailConfig = mailConfigService.getEnabledMailConfig();
            if (mailConfig == null) {
                log.warn("未找到启用的邮箱配置，邮件归档任务终止");
                return;
            }
            
            // 创建邮件会话
            Properties props = new Properties();
            if ("pop3".equalsIgnoreCase(mailConfig.getProtocol())) {
                props.setProperty("mail.store.protocol", "pop3");
                props.setProperty("mail.pop3.host", mailConfig.getHost());
                props.setProperty("mail.pop3.port", String.valueOf(mailConfig.getPort()));
                
                // 是否启用SSL
                if (mailConfig.getSsl()) {
                    props.setProperty("mail.pop3.ssl.enable", "true");
                }
            } else if ("imap".equalsIgnoreCase(mailConfig.getProtocol())) {
                props.setProperty("mail.store.protocol", "imap");
                props.setProperty("mail.imap.host", mailConfig.getHost());
                props.setProperty("mail.imap.port", String.valueOf(mailConfig.getPort()));
                
                // 是否启用SSL
                if (mailConfig.getSsl()) {
                    props.setProperty("mail.imap.ssl.enable", "true");
                }
            } else {
                log.error("不支持的邮件协议：{}", mailConfig.getProtocol());
                return;
            }
            
            // 创建会话
            Session session = Session.getInstance(props);
            
            // 连接到邮箱服务器
            Store store = session.getStore();
            store.connect(mailConfig.getHost(), mailConfig.getEmail(), mailConfig.getPassword());
            
            // 打开收件箱
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            
            try {
                // 获取需要归档的邮件
                List<Message> messages = getArchiveMessages(folder, mailConfig.getEmail());
                
                // 处理邮件
                StringBuilder processedUids = new StringBuilder();
                for (Message message : messages) {
                    try {
                        String uid = ((POP3Folder) folder).getUID(message);
                        saveMailContent(message, mailConfig.getEmail());
                        if (processedUids.length() > 0) {
                            processedUids.append(",");
                        }
                        processedUids.append(uid);
                    } catch (Exception e) {
                        log.error("处理邮件失败: {}", e.getMessage(), e);
                    }
                }
                
                // 更新本轮处理的邮件ID列表
                if (processedUids.length() > 0) {
                    mailIdsCheckService.updateMailIds(mailConfig.getEmail(), processedUids.toString());
                }
                
                log.info("邮件归档任务完成，共处理{}封邮件", messages.size());
            } finally {
                // 关闭连接
                folder.close(false);
                store.close();
            }
        } catch (Exception e) {
            log.error("执行邮件归档任务失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void triggerArchivingForAllActiveConfigs() {
        log.info("开始触发所有活动配置的归档...");
        List<MailConfig> activeConfigs = mailConfigService.findAllActive();
        for (MailConfig config : activeConfigs) {
            try {
                executeMailArchiveTask();
            } catch (Exception e) {
                log.error("处理邮箱配置失败: {}, 错误: {}", config.getEmail(), e.getMessage(), e);
            }
        }
        log.info("所有活动配置的归档触发完成");
    }

    private Properties createMailProperties(MailConfig mailConfig) {
        Properties props = new Properties();
        String protocol = mailConfig.getProtocol().toLowerCase();
        
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail." + protocol + ".host", mailConfig.getHost());
        props.setProperty("mail." + protocol + ".port", String.valueOf(mailConfig.getPort()));
        
        if (mailConfig.getSsl()) {
            props.setProperty("mail." + protocol + ".ssl.enable", "true");
            props.setProperty("mail." + protocol + ".ssl.trust", "*");
        }
        
        return props;
    }
}