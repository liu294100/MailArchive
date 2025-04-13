package org.apollo.mail.repository;

import org.apollo.mail.entity.ArchiveEmail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ArchiveEmailRepository extends JpaRepository<ArchiveEmail, Long> {
    Optional<ArchiveEmail> findByUid(String uid);

    @Query("SELECT ae FROM ArchiveEmail ae WHERE " +
           "(:fromEmail IS NULL OR ae.fromEmail LIKE %:fromEmail%) AND " +
           "(:startTime IS NULL OR ae.receiveTime >= :startTime) AND " +
           "(:endTime IS NULL OR ae.receiveTime <= :endTime)")
    Page<ArchiveEmail> searchEmails(
            @Param("fromEmail") String fromEmail,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
} 