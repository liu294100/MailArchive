package org.apollo.mail.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "archive_email")
public class ArchiveEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uid;

    @Column(name = "from_email", nullable = false)
    private String fromEmail;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    @Column(name = "has_attachment")
    private Boolean hasAttachment = false;

    @Column(name = "attachment_file_paths", columnDefinition = "TEXT")
    private String attachmentFilePaths;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mailbox_config_id")
    private MailboxConfig mailboxConfig;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 