package org.apollo.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apollo.mail.entity.MailIdsCheck;

/**
 * 邮件ID检查服务接口
 */
public interface MailIdsCheckService extends IService<MailIdsCheck> {
    
    /**
     * 获取指定邮箱类型的邮件ID列表
     * 
     * @param mailType 邮件类型
     * @return 邮件ID列表字符串，多个ID以逗号分隔
     */
    String getMailIds(String mailType);
    
    /**
     * 更新指定邮箱类型的邮件ID列表
     * 
     * @param mailType 邮件类型
     * @param mailIds 邮件ID列表字符串，多个ID以逗号分隔
     */
    void updateMailIds(String mailType, String mailIds);
    
    /**
     * 检查邮件ID是否已存在
     * 
     * @param mailType 邮件类型
     * @param mailId 邮件ID
     * @return 是否存在
     */
    boolean isMailIdExists(String mailType, String mailId);
    
    /**
     * 添加单个邮件ID到列表
     * 
     * @param mailType 邮件类型
     * @param mailId 邮件ID
     */
    void addMailId(String mailType, String mailId);

    /**
     * 检查邮件是否已处理
     */
    boolean isMailProcessed(String mailId, Long mailboxId);

    /**
     * 标记邮件为已处理
     */
    void markMailAsProcessed(String mailId, Long mailboxId);
}