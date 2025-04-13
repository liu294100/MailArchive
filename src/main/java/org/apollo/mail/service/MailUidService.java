package org.apollo.mail.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 邮件UID服务接口
 * 用于管理已处理邮件的UID集合，防止重复处理
 */
public interface MailUidService {
    
    /**
     * 获取指定邮件类型的所有邮件UID集合
     * @param mailType 邮件类型
     * @return UID集合
     * @throws Exception 异常
     */
    Set<String> getMailUids(String mailType) throws Exception;
    
    /**
     * 获取指定邮件类型最近几个月的邮件UID集合
     * @param mailType 邮件类型
     * @param months 月数
     * @return UID集合
     * @throws Exception 异常
     */
    Set<String> getRecentMailUids(String mailType, int months) throws Exception;
    
    /**
     * 检查指定邮件类型的邮件UID是否存在
     * @param mailType 邮件类型
     * @param mailUid 邮件UID
     * @return 是否存在
     * @throws Exception 异常
     */
    boolean isMailUidExists(String mailType, String mailUid) throws Exception;
    
    /**
     * 添加邮件UID (此方法可能仅为接口完整性，实际逻辑在保存邮件时处理)
     * @param mailType 邮件类型
     * @param mailUid 邮件UID
     * @throws Exception 异常
     */
    void addMailUid(String mailType, String mailUid) throws Exception;

    /**
     * 按主题搜索邮件
     */
    List<Map<String, Object>> searchSubjectList(String mailType, String subject);

    /**
     * 按发件人搜索邮件
     */
    List<Map<String, Object>> searchSenderList(String mailType, String sender);

    /**
     * 按收件人搜索邮件
     */
    List<Map<String, Object>> searchRecipientList(String mailType, String recipient);

    /**
     * 按接收日期范围搜索邮件
     */
    List<Map<String, Object>> searchByReceiveDate(String mailType, String startDay, String endDay);
}