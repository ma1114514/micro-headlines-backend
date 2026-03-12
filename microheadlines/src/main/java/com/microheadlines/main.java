package com.microheadlines;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.microheadlines.mapper")

@SpringBootApplication
public class main {
    public static void main(String[] args) {
        System.out.println("helloSpring");
        SpringApplication.run(main.class,args);
    }
}
