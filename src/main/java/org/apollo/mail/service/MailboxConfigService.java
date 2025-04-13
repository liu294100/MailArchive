package org.apollo.mail.service;

import org.apollo.mail.entity.MailboxConfig;
import java.util.List;

public interface MailboxConfigService {
    List<MailboxConfig> getAllConfigs();
    MailboxConfig getConfig(Long id);
    MailboxConfig addConfig(MailboxConfig config);
    MailboxConfig updateConfig(Long id, MailboxConfig config);
    void deleteConfig(Long id);
    List<MailboxConfig> getActiveConfigs();
} 