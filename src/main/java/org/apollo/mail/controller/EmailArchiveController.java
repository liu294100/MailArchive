package org.apollo.mail.controller;

import org.apollo.mail.entity.ArchiveEmail;
import org.apollo.mail.service.EmailArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/archive")
public class EmailArchiveController {

    @Autowired
    private EmailArchiveService emailArchiveService;

    @PostMapping("/mailbox/{mailboxId}")
    public ResponseEntity<?> archiveEmails(@PathVariable Long mailboxId) {
        emailArchiveService.archiveEmails(mailboxId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public Page<ArchiveEmail> searchEmails(
            @RequestParam(required = false) String fromEmail,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            Pageable pageable) {
        return emailArchiveService.searchEmails(fromEmail, startTime, endTime, pageable);
    }
} 