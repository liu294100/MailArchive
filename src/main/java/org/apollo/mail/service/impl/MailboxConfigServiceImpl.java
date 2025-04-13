package org.apollo.mail.service.impl;

import org.apollo.mail.entity.MailboxConfig;
import org.apollo.mail.repository.MailboxConfigRepository;
import org.apollo.mail.service.MailboxConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MailboxConfigServiceImpl implements MailboxConfigService {

    @Autowired
    private MailboxConfigRepository mailboxConfigRepository;

    @Override
    public List<MailboxConfig> getAllConfigs() {
        return mailboxConfigRepository.findAll();
    }

    @Override
    public MailboxConfig getConfig(Long id) {
        return mailboxConfigRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Mailbox config not found with id: " + id));
    }

    @Override
    @Transactional
    public MailboxConfig addConfig(MailboxConfig config) {
        return mailboxConfigRepository.save(config);
    }

    @Override
    @Transactional
    public MailboxConfig updateConfig(Long id, MailboxConfig config) {
        MailboxConfig existingConfig = getConfig(id);
        config.setId(existingConfig.getId());
        return mailboxConfigRepository.save(config);
    }

    @Override
    @Transactional
    public void deleteConfig(Long id) {
        mailboxConfigRepository.deleteById(id);
    }

    @Override
    public List<MailboxConfig> getActiveConfigs() {
        return mailboxConfigRepository.findByActiveTrue();
    }
} 