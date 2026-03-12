package com.microheadlines.mapper;

import com.microheadlines.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface UserMapper {
    //查所有
    @Select("select * from user")
    List<User> findAll();
    //id查
    @Select("select * from user where id=#{id}")
    User findById(Long id);
    //name查
    @Select("select * from user where username=#{name}")
    User findByName(String name);
    //添加一个要求id,密码，名字
    @Insert("insert into user (username, password, nick_name) " +
            "VALUES (#{username},#{password},#{nickName})")
    int addUser(User user);

    @Insert("insert into user (username, password)" +
            "VALUES (#{username},#{password})")
    int addRegistUser(User user);
    //改名sql
    @Update("update user set nick_name=#{nickName} where id=#{id}")
    int resetName(String nickName,Long id);
}
