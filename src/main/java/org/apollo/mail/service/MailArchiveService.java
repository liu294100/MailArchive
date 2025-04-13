package org.apollo.mail.service;

import javax.mail.Folder;
import javax.mail.Message;
import java.util.List;

public interface MailArchiveService {
    /**
     * 获取需要归档的邮件列表
     */
    List<Message> getArchiveMessages(Folder folder, String mailType) throws Exception;

    /**
     * 保存邮件内容和附件
     */
    void saveMailContent(Message message, String mailType) throws Exception;

    /**
     * 执行邮件归档任务
     */
    void executeMailArchiveTask();

    /**
     * 触发所有活动配置的归档
     */
    void triggerArchivingForAllActiveConfigs();
}