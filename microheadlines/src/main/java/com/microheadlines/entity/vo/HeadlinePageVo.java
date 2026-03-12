package com.microheadlines.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadlinePageVo implements Serializable {
    //hid  title  type pageView  pastHour  publisher
    private Integer hid;
    private String title;
    private Integer type;
    private Integer pageView;
    private Long pastHour;
    private Integer publisher;

}
