package com.microheadlines.entity;

import lombok.Data;

@Data
public class SysUser {
    private Integer uid;
    private String username;
    private String userPwd;
}
