package org.apollo.mail.service;

import org.apollo.mail.entity.ArchiveEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface EmailArchiveService {
    void archiveEmails(Long mailboxConfigId);
    Page<ArchiveEmail> searchEmails(String fromEmail, Date startTime, Date endTime, Pageable pageable);
    void processMailbox(Long mailboxConfigId);
} 