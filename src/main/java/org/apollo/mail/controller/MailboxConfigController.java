package org.apollo.mail.controller;

import org.apollo.mail.entity.MailboxConfig;
import org.apollo.mail.service.MailboxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/mailbox")
public class MailboxConfigController {

    @Autowired
    private MailboxConfigService mailboxConfigService;

    @GetMapping
    public List<MailboxConfig> getAllConfigs() {
        return mailboxConfigService.getAllConfigs();
    }

    @GetMapping("/{id}")
    public MailboxConfig getConfig(@PathVariable Long id) {
        return mailboxConfigService.getConfig(id);
    }

    @PostMapping
    public MailboxConfig addConfig(@Valid @RequestBody MailboxConfig config) {
        return mailboxConfigService.addConfig(config);
    }

    @PutMapping("/{id}")
    public MailboxConfig updateConfig(@PathVariable Long id, @Valid @RequestBody MailboxConfig config) {
        return mailboxConfigService.updateConfig(id, config);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteConfig(@PathVariable Long id) {
        mailboxConfigService.deleteConfig(id);
        return ResponseEntity.ok().build();
    }
} 