package com.microheadlines.service;

import com.microheadlines.entity.News;
import com.microheadlines.entity.vo.HeadlineQueryVo;

import java.util.Map;

public interface HeadlineService {
    Map findPage(HeadlineQueryVo headlineQueryVo);

    News findNewsById(int id);
}
