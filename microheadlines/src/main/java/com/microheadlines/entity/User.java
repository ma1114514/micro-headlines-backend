package com.microheadlines.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String nickName;

}
