package org.apollo.mail.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("mail_config")
public class MailConfig {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String protocol;
    
    private String host;
    
    private Integer port;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private Boolean ssl = false;
    
    private Boolean active = true;
    
    private String description;
    
    private LocalDateTime lastSyncTime;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
} 