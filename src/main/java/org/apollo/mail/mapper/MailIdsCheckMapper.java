package org.apollo.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apollo.mail.entity.MailIdsCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MailIdsCheckMapper extends BaseMapper<MailIdsCheck> {
    @Select("SELECT * FROM mail_ids_check WHERE mail_id = #{mailId} AND mailbox_id = #{mailboxId}")
    MailIdsCheck findByMailIdAndMailboxId(@Param("mailId") String mailId, @Param("mailboxId") Long mailboxId);

    @Update("UPDATE mail_ids_check SET processed = true WHERE mail_id = #{mailId} AND mailbox_id = #{mailboxId}")
    int markAsProcessed(@Param("mailId") String mailId, @Param("mailboxId") Long mailboxId);
} 