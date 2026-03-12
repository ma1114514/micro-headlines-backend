package com.microheadlines.controller;

import com.microheadlines.common.Result;
import com.microheadlines.common.ResultCodeEnum;
import com.microheadlines.entity.News;
import com.microheadlines.service.impl.NewsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("news")
public class NewsController {
    @Autowired
    NewsServiceImpl newsService;

    @PostMapping("addNews")
    protected Result addNews(@RequestBody News news){
        int i = newsService.addNews(news);
        if(i!=1){
            return Result.build(null, ResultCodeEnum.UNKNOWERROR);
        }
        return Result.ok(null);
    }
}
