package org.apollo.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@TableName("mail_ids_check")
public class MailIdsCheck {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String mailType;
    
    private String mailIds;
    
    private String maxMailId;
    
    private Long mailboxId;
    
    private Boolean processed;
    
    private Date updateTime;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 