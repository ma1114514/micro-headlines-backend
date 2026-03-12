package com.microheadlines.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    private Integer id;
    private String title;
    private String content;
    private String category;
    private Integer authorId;
    private Integer views;
    private Date createTime;
    private Date updateTime;
    private Integer isDeleted;
}
