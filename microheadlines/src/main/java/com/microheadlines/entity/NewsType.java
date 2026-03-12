package com.microheadlines.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class NewsType implements Serializable{
    private Integer tId;
    private String tName;
}
