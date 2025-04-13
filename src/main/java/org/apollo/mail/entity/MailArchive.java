package org.apollo.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("mail_archive")
public class MailArchive {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String mailUid;
    
    private String mailType;
    
    private String subject;
    
    private String sender;
    
    private String recipient;
    
    private String cc;
    
    private String body;
    
    private String hasAttachment;  // 使用String类型: "0" - 无附件, "1" - 有附件
    
    private String attachmentPath;
    
    private Date sendDate;
    
    private Date receiveDate;
    
    private String receiveYm;  // 接收年月，格式：YYYYMM
    
    private String receiveDay; // 接收日，格式：DD
    
    private String receiveHour; // 接收小时，格式：HH
    
    private Long mailboxId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Date updateTime;
} 