package org.apollo.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apollo.mail.entity.MailConfig;
import org.apollo.mail.mapper.MailConfigMapper;
import org.apollo.mail.service.MailConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 邮箱配置 服务实现类
 */
@Service
public class MailConfigServiceImpl extends ServiceImpl<MailConfigMapper, MailConfig> implements MailConfigService {

    @Override
    public MailConfig getEnabledMailConfig() {
        return this.list(new LambdaQueryWrapper<MailConfig>()
                .eq(MailConfig::getActive, true)
                .last("LIMIT 1"))
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<MailConfig> findAllActive() {
        return this.list(new LambdaQueryWrapper<MailConfig>()
                .eq(MailConfig::getActive, true));
    }

    @Override
    public MailConfig getConfig(Long id) {
        return this.getById(id);
    }

    @Override
    public List<MailConfig> findActiveConfigs() {
        return this.list(new LambdaQueryWrapper<MailConfig>()
                .eq(MailConfig::getActive, true));
    }
}