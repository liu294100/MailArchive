package org.apollo.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apollo.mail.entity.MailArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface MailArchiveMapper extends BaseMapper<MailArchive> {
    @Select("SELECT * FROM mail_archive WHERE mail_id = #{mailId} AND mailbox_id = #{mailboxId}")
    MailArchive findByMailIdAndMailboxId(@Param("mailId") String mailId, @Param("mailboxId") Long mailboxId);

    @Select("SELECT COUNT(*) FROM mail_archive WHERE mailbox_id = #{mailboxId}")
    int countByMailboxId(@Param("mailboxId") Long mailboxId);

    // Add methods for search operations
    @Select("<script>" +
            "SELECT * FROM mail_archive WHERE mail_type = #{mailType} " +
            "<if test='subject != null and subject != \"\"'> AND subject LIKE CONCAT('%', #{subject}, '%')</if>" +
            " ORDER BY receive_date DESC" +
            "</script>")
    List<Map<String, Object>> searchSubjectList(@Param("mailType") String mailType, @Param("subject") String subject);

    @Select("<script>" +
            "SELECT * FROM mail_archive WHERE mail_type = #{mailType} " +
            "<if test='sender != null and sender != \"\"'> AND sender LIKE CONCAT('%', #{sender}, '%')</if>" +
            " ORDER BY receive_date DESC" +
            "</script>")
    List<Map<String, Object>> searchSenderList(@Param("mailType") String mailType, @Param("sender") String sender);

    @Select("<script>" +
            "SELECT * FROM mail_archive WHERE mail_type = #{mailType} " +
            "<if test='recipient != null and recipient != \"\"'> AND recipient LIKE CONCAT('%', #{recipient}, '%')</if>" +
            " ORDER BY receive_date DESC" +
            "</script>")
    List<Map<String, Object>> searchRecipientList(@Param("mailType") String mailType, @Param("recipient") String recipient);

    @Select("<script>" +
            "SELECT * FROM mail_archive WHERE mail_type = #{mailType} " +
            "<if test='startDay != null and startDay != \"\"'> AND receive_date &gt;= STR_TO_DATE(#{startDay}, '%Y%m%d')</if>" +
            "<if test='endDay != null and endDay != \"\"'> AND receive_date &lt;= STR_TO_DATE(#{endDay}, '%Y%m%d')</if>" +
            " ORDER BY receive_date DESC" +
            "</script>")
    List<Map<String, Object>> searchByReceiveDate(@Param("mailType") String mailType, @Param("startDay") String startDay, @Param("endDay") String endDay);
} 