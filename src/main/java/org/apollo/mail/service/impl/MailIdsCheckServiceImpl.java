package org.apollo.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apollo.mail.entity.MailIdsCheck;
import org.apollo.mail.mapper.MailIdsCheckMapper;
import org.apollo.mail.service.MailIdsCheckService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;

/**
 * 邮件ID检查服务实现类
 */
@Slf4j
@Service
@Transactional
public class MailIdsCheckServiceImpl extends ServiceImpl<MailIdsCheckMapper, MailIdsCheck> implements MailIdsCheckService {

    @Override
    public String getMailIds(String mailType) {
        String mailIds = "";
        try {
            LambdaQueryWrapper<MailIdsCheck> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MailIdsCheck::getMailType, mailType);
            List<MailIdsCheck> list = list(queryWrapper);
            
            if (list != null && !list.isEmpty() && list.get(0) != null) {
                mailIds = list.get(0).getMailIds();
                if (mailIds == null) {
                    mailIds = "";
                }
            }
        } catch (Exception e) {
            log.error("获取邮件ID列表异常", e);
        }
        return mailIds;
    }

    @Override
    public void updateMailIds(String mailType, String mailIds) {
        if (StringUtils.hasText(mailIds)) {
            try {
                LambdaQueryWrapper<MailIdsCheck> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(MailIdsCheck::getMailType, mailType);
                
                MailIdsCheck entity = getOne(queryWrapper);
                if (entity == null) {
                    // 不存在则创建新记录
                    entity = new MailIdsCheck();
                    entity.setMailType(mailType);
                    entity.setMailIds(mailIds);
                    entity.setUpdateTime(new Date());
                    save(entity);
                } else {
                    // 存在则更新
                    entity.setMailIds(mailIds);
                    entity.setUpdateTime(new Date());
                    updateById(entity);
                }
            } catch (Exception e) {
                log.error("更新邮件ID列表异常", e);
            }
        }
    }

    @Override
    public boolean isMailIdExists(String mailType, String mailId) {
        if (!StringUtils.hasText(mailId)) {
            return false;
        }
        
        String mailIds = getMailIds(mailType);
        if (!StringUtils.hasText(mailIds)) {
            return false;
        }
        
        // 检查邮件ID是否在列表中
        String[] ids = mailIds.split(",");
        for (String id : ids) {
            if (mailId.equals(id.trim())) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void addMailId(String mailType, String mailId) {
        if (!StringUtils.hasText(mailId)) {
            return;
        }
        
        // 如果已存在，不需要添加
        if (isMailIdExists(mailType, mailId)) {
            return;
        }
        
        String mailIds = getMailIds(mailType);
        String newMailIds;
        
        if (StringUtils.hasText(mailIds)) {
            newMailIds = mailIds + "," + mailId;
        } else {
            newMailIds = mailId;
        }
        
        // 更新邮件ID列表
        updateMailIds(mailType, newMailIds);
        
        // 更新最大邮件ID
        updateMaxMailId(mailType, mailId);
    }
    
    /**
     * 更新最大邮件ID
     * 
     * @param mailType 邮件类型
     * @param mailId 邮件ID
     */
    private void updateMaxMailId(String mailType, String mailId) {
        try {
            LambdaQueryWrapper<MailIdsCheck> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MailIdsCheck::getMailType, mailType);
            
            MailIdsCheck entity = getOne(queryWrapper);
            if (entity != null) {
                entity.setMaxMailId(mailId);
                entity.setUpdateTime(new Date());
                updateById(entity);
            }
        } catch (Exception e) {
            log.error("更新最大邮件ID异常", e);
        }
    }

    @Override
    public boolean isMailProcessed(String mailId, Long mailboxId) {
        // ... existing code ...
        return false; // Placeholder return, actual implementation needed
    }

    @Override
    public void markMailAsProcessed(String mailId, Long mailboxId) {
        MailIdsCheck check = this.getOne(new LambdaQueryWrapper<MailIdsCheck>()
                .eq(MailIdsCheck::getMailboxId, mailboxId)
                .last("LIMIT 1"));
        
        if (check == null) {
            check = new MailIdsCheck();
            check.setMailboxId(mailboxId);
            check.setMailIds(mailId); // Store the single processed ID
            check.setProcessed(true);
            check.setUpdateTime(new Date());
            this.save(check);
        } else {
            String existingIds = check.getMailIds();
            if (existingIds == null || existingIds.isEmpty()) {
                check.setMailIds(mailId);
            } else if (!existingIds.contains(mailId)) {
                // Append the new ID, ensuring no duplicates
                Set<String> ids = new HashSet<>(Arrays.asList(existingIds.split(",")));
                ids.add(mailId);
                check.setMailIds(String.join(",", ids));
            }
            check.setProcessed(true); // Mark as processed
            check.setUpdateTime(new Date());
            this.updateById(check);
        }
    }
}