package org.apollo.mail.controller;

import org.apollo.mail.entity.MailboxConfig;
import org.apollo.mail.service.MailboxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mailbox")
public class MailboxConfigController {

    @Autowired
    private MailboxConfigService mailboxConfigService;

    // GET all configurations (useful for JS refresh)
    @GetMapping
    public List<MailboxConfig> getAllConfigs() {
        return mailboxConfigService.getAllConfigs();
    }

    // GET a single configuration (useful for edit form)
    @GetMapping("/{id}")
    public ResponseEntity<MailboxConfig> getConfigById(@PathVariable Long id) {
        MailboxConfig config = mailboxConfigService.getConfig(id);
        if (config != null) {
            return ResponseEntity.ok(config);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // POST to add a new configuration
    @PostMapping
    public ResponseEntity<MailboxConfig> addConfig(@RequestBody MailboxConfig config) {
        // Ensure ID is null so it's treated as a new entry
        config.setId(null); 
        MailboxConfig savedConfig = mailboxConfigService.addConfig(config);
        return ResponseEntity.ok(savedConfig);
    }

    // PUT to update an existing configuration
    @PutMapping("/{id}")
    public ResponseEntity<MailboxConfig> updateConfig(@PathVariable Long id, @RequestBody MailboxConfig config) {
        MailboxConfig updatedConfig = mailboxConfigService.updateConfig(id, config);
        if (updatedConfig != null) {
            return ResponseEntity.ok(updatedConfig);
        } else {
            // Could return not found if the ID doesn't exist
            return ResponseEntity.notFound().build(); 
        }
    }

    // DELETE a configuration
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        mailboxConfigService.deleteConfig(id);
        return ResponseEntity.ok().build();
    }
} 