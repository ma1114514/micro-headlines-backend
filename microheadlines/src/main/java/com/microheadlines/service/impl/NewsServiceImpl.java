package com.microheadlines.service.impl;

import com.microheadlines.entity.News;
import com.microheadlines.mapper.NewsMapper;
import com.microheadlines.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class NewsServiceImpl implements NewsService {
    @Autowired
    NewsMapper newsMapper;
    @Override
    public int addNews(News news) {
        return newsMapper.addNews(news);
    }
}
