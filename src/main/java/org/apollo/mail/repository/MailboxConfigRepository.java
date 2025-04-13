package org.apollo.mail.repository;

import org.apollo.mail.entity.MailboxConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailboxConfigRepository extends JpaRepository<MailboxConfig, Long> {
    List<MailboxConfig> findByActiveTrue();
} 