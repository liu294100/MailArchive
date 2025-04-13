package org.apollo.mail.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apollo.mail.entity.MailConfig;

import java.util.List;

/**
 * 邮箱配置 服务类
 */
public interface MailConfigService extends IService<MailConfig> {

    /**
     * 获取所有激活的邮箱配置
     * @return 激活的配置列表
     */
    List<MailConfig> findActiveConfigs();

    /**
     * 获取启用状态的邮件配置
     * @return 邮件配置列表
     */
    MailConfig getEnabledMailConfig();

    /**
     * 获取所有活动的邮箱配置
     */
    List<MailConfig> findAllActive();

    /**
     * 根据ID获取邮箱配置
     */
    MailConfig getConfig(Long id);
}