package com.microheadlines.mapper;

import com.microheadlines.entity.User;
import org.apache.ibatis.annotations.Select;

public interface SysUserMapper {
    @Select("select * from sys_user where uid=#{id}")
    User selectById(Long id);
}
