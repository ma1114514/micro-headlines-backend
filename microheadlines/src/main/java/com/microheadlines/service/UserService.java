package com.microheadlines.service;

import com.microheadlines.entity.User;

public interface UserService {
    User findByUsername(String username);

    User findByUserId(String id);

    int addUser(User user);

    int addRegistUser(User user);


}
