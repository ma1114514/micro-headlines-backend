package com.microheadlines.controller;

import com.microheadlines.entity.User;
import com.microheadlines.mapper.SysUserMapper;
import com.microheadlines.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user01")
public class SysUserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SysUserMapper sysUserMapper;
    @PostMapping("getOne")
    public User getOne() {
        System.out.println(sysUserMapper.selectById(6L));
        return sysUserMapper.selectById(6L);
    }
    @Autowired
    private UserMapper userMapper;
    @GetMapping("getAll")
    public List<User> getAll(){
        System.out.println(userMapper.findById(1L));
        return userMapper.findAll();

    }
    @GetMapping("testInsert")
    public int insert(){
        User user04 = new User(null, "test04", "test04", "test04");
        return userMapper.addUser(user04);
    }
    @GetMapping("getById")
    public User getById(){
        return userMapper.findById(1L);
    }
    @GetMapping("testUpdate")
    public int update(){
        return  userMapper.resetName("test09",7L);
    }




}
