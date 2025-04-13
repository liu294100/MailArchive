package org.apollo.mail.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apollo.mail.entity.MailArchive;
import org.apollo.mail.mapper.MailArchiveMapper;
import org.apollo.mail.service.MailUidService;
import org.apollo.mail.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 邮件UID服务实现类
 */
@Slf4j
@Service
@Transactional
public class MailUidServiceImpl implements MailUidService {

    @Autowired
    private MailArchiveMapper mailArchiveMapper;
    
    @Override
    public Set<String> getMailUids(String mailType) throws Exception {
        log.info("获取邮件UID集合，邮件类型：{}", mailType);
        if (mailType == null || mailType.trim().isEmpty()) {
            log.warn("邮件类型为空，无法获取邮件UID集合");
            return new HashSet<>();
        }

        QueryWrapper<MailArchive> countWrapper = new QueryWrapper<>();
        countWrapper.eq("mail_type", mailType);
        Long count = mailArchiveMapper.selectCount(countWrapper);

        if (count == null) count = 0L;

        if (count > 200000) {
            log.info("邮件数量过多({}), 只获取最近三个月的邮件UID", count);
            return getRecentMailUids(mailType, 3);
        }
        
        QueryWrapper<MailArchive> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT mail_uid").eq("mail_type", mailType);
        List<Map<String, Object>> list = mailArchiveMapper.selectMaps(queryWrapper);
        
        Set<String> uids = list.stream()
                               .map(map -> String.valueOf(map.get("mail_uid")))
                               .collect(Collectors.toSet());
        
        log.info("获取到{}封邮件的UID", uids.size());
        return uids;
    }
    
    @Override
    public Set<String> getRecentMailUids(String mailType, int months) throws Exception {
        log.info("获取最近{}个月的邮件UID集合，邮件类型：{}", months, mailType);
        if (mailType == null || mailType.trim().isEmpty()) {
            log.warn("邮件类型为空，无法获取最近邮件UID集合");
            return new HashSet<>();
        }
        
        Date currentDate = new Date();
        SimpleDateFormat ymFormat = new SimpleDateFormat("yyyy-MM");
        List<String> ymList = new ArrayList<>();
        for (int i = 0; i < months; i++) {
            Date monthDate = DateUtils.addMonths(currentDate, -i);
            ymList.add(ymFormat.format(monthDate));
        }
        
        QueryWrapper<MailArchive> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT mail_uid")
                    .eq("mail_type", mailType)
                    .in("receive_ym", ymList); // Use receive_ym field assumed to exist
                    
        List<Map<String, Object>> list = mailArchiveMapper.selectMaps(queryWrapper);
        
        Set<String> uids = list.stream()
                               .map(map -> String.valueOf(map.get("mail_uid")))
                               .collect(Collectors.toSet());
        
        log.info("获取到最近{}个月的{}封邮件UID", months, uids.size());
        return uids;
    }
    
    @Override
    public boolean isMailUidExists(String mailType, String mailUid) throws Exception {
        if (mailType == null || mailType.trim().isEmpty() || mailUid == null || mailUid.trim().isEmpty()) {
            log.warn("邮件类型或UID为空，无法检查邮件UID是否存在");
            return false;
        }
        
        QueryWrapper<MailArchive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mail_type", mailType).eq("mail_uid", mailUid);
        Long count = mailArchiveMapper.selectCount(queryWrapper);
        
        return count != null && count > 0;
    }
    
    @Override
    public void addMailUid(String mailType, String mailUid) throws Exception {
        if (mailType == null || mailType.trim().isEmpty() || mailUid == null || mailUid.trim().isEmpty()) {
            log.warn("邮件类型或UID为空，无法添加邮件UID");
            return;
        }
        log.info("添加邮件UID：{}，邮件类型：{}", mailUid, mailType);
        // Usually, this logic is handled during the saveMailContent process, 
        // so this method might just log or do nothing based on requirements.
    }

    @Override
    public List<Map<String, Object>> searchSubjectList(String mailType, String subject) {
        return mailArchiveMapper.searchSubjectList(mailType, subject);
    }

    @Override
    public List<Map<String, Object>> searchSenderList(String mailType, String sender) {
        return mailArchiveMapper.searchSenderList(mailType, sender);
    }

    @Override
    public List<Map<String, Object>> searchRecipientList(String mailType, String recipient) {
        return mailArchiveMapper.searchRecipientList(mailType, recipient);
    }

    @Override
    public List<Map<String, Object>> searchByReceiveDate(String mailType, String startDay, String endDay) {
        return mailArchiveMapper.searchByReceiveDate(mailType, startDay, endDay);
    }
}