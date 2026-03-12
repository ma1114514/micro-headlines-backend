package com.microheadlines.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadlineQueryVo implements Serializable {
    private String keyWord;
    private String type;
    private Integer pageNum;
    private Integer pageSize;
}
