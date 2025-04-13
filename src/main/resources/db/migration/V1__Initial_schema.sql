-- 创建数据库 (如果尚未创建)
-- CREATE DATABASE IF NOT EXISTS mail_archive CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE mail_archive;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码 (应存储哈希值)',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `enabled` tinyint(1) DEFAULT '1' COMMENT '是否启用 (1:是, 0:否)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户信息表';

-- ----------------------------
-- Table structure for mail_config
-- ----------------------------
DROP TABLE IF EXISTS `mail_config`;
CREATE TABLE `mail_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `protocol` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '协议 (如 imap, pop3)',
  `host` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮件服务器主机',
  `port` int(11) NOT NULL COMMENT '端口',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱账号',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱密码或授权码',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱地址 (用于显示或标识)',
  `ssl` tinyint(1) DEFAULT '0' COMMENT '是否启用SSL (1:是, 0:否)',
  `active` tinyint(1) DEFAULT '1' COMMENT '是否启用此配置 (1:是, 0:否)',
  `description` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `last_sync_time` datetime DEFAULT NULL COMMENT '上次同步时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_active` (`active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮箱配置表';

-- ----------------------------
-- Table structure for mail_archive
-- ----------------------------
DROP TABLE IF EXISTS `mail_archive`;
CREATE TABLE `mail_archive` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mail_uid` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮件在服务器上的唯一ID',
  `mail_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮件类型 (可能用于区分不同邮箱配置下的邮件)',
  `subject` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮件主题',
  `sender` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发件人邮箱',
  `recipient` text COLLATE utf8mb4_unicode_ci COMMENT '收件人列表 (逗号分隔)',
  `cc` text COLLATE utf8mb4_unicode_ci COMMENT '抄送列表 (逗号分隔)',
  `body` longtext COLLATE utf8mb4_unicode_ci COMMENT '邮件正文内容 (HTML或纯文本)',
  `has_attachment` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '是否有附件 (1:是, 0:否)',
  `attachment_path` text COLLATE utf8mb4_unicode_ci COMMENT '附件存储路径 (分号分隔)',
  `send_date` datetime DEFAULT NULL COMMENT '邮件发送时间',
  `receive_date` datetime DEFAULT NULL COMMENT '邮件接收时间',
  `receive_ym` varchar(6) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收年月 (格式: YYYYMM)',
  `receive_day` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收日 (格式: DD)',
  `receive_hour` varchar(2) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收小时 (格式: HH)',
  `mailbox_id` bigint(20) DEFAULT NULL COMMENT '关联的邮箱配置ID',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '归档创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '归档更新时间',
  `update_time` datetime DEFAULT NULL COMMENT '记录更新时间 (冗余字段，同updated_at)',
  PRIMARY KEY (`id`),
  KEY `idx_mailbox_id` (`mailbox_id`),
  KEY `idx_sender` (`sender`),
  KEY `idx_receive_date` (`receive_date`),
  KEY `idx_mail_type_uid` (`mail_type`,`mail_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件归档表';

-- ----------------------------
-- Table structure for mail_ids_check
-- ----------------------------
DROP TABLE IF EXISTS `mail_ids_check`;
CREATE TABLE `mail_ids_check` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mail_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮件类型或邮箱标识',
  `mail_ids` text COLLATE utf8mb4_unicode_ci COMMENT '已处理邮件ID列表 (逗号分隔)',
  `max_mail_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '处理过的最大邮件ID (如果适用)',
  `mailbox_id` bigint(20) DEFAULT NULL COMMENT '关联的邮箱配置ID',
  `processed` tinyint(1) DEFAULT '0' COMMENT '是否已处理标记 (冗余字段, 实际通过mail_ids判断)',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mailbox_id` (`mailbox_id`),
  KEY `idx_mail_type` (`mail_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='邮件ID检查表 (用于增量同步)'; 