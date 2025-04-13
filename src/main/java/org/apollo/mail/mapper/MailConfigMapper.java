package org.apollo.mail.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apollo.mail.entity.MailConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MailConfigMapper extends BaseMapper<MailConfig> {
    @Select("SELECT * FROM mail_config WHERE active = true")
    List<MailConfig> findAllActive();

    @Update("UPDATE mail_config SET active = #{active} WHERE id = #{id}")
    int updateActive(Long id, Boolean active);
} 