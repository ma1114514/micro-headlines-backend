package com.microheadlines.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")

public class HelloController {
    @GetMapping("/spring")
    protected void hello(){
        System.out.println("helloSpring");
    }
}
