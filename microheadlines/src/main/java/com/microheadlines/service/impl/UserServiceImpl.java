package com.microheadlines.service.impl;

import com.microheadlines.entity.User;
import com.microheadlines.mapper.UserMapper;
import com.microheadlines.service.UserService;
import com.microheadlines.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Override
    public User findByUsername(String username) {
        return userMapper.findByName(username);
    }

    @Override
    public User findByUserId(String id) {
        return  userMapper.findById(Long.parseLong(id));
    }

    @Override
    public int addUser(User user) {return userMapper.addUser(user);}

    @Override
    public int addRegistUser(User user) {
        //对密码进行加密
        user.setPassword(AESUtil.encrypt(user.getPassword()));
        return userMapper.addRegistUser(user);
    }


}
