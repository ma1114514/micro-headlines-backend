package com.microheadlines.service.impl;

import com.microheadlines.entity.News;
import com.microheadlines.entity.vo.HeadlineQueryVo;
import com.microheadlines.mapper.HeadlineNewsMapper;
import com.microheadlines.service.HeadlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class HeadlineServiceImpl implements HeadlineService {
    @Autowired
    HeadlineNewsMapper headLineNewsMapper;
    @Override
    public Map findPage(HeadlineQueryVo headlineQueryVo) {
        Map pageInfo=new HashMap();
        System.out.println("ServiceStart");
        Long countSum = headLineNewsMapper.findCount(headlineQueryVo);
        //查询到的所有符合条件的新闻信息
        pageInfo.put("pageData",headLineNewsMapper.findPageList(headlineQueryVo.getType(),
                headlineQueryVo.getKeyWord(),headlineQueryVo.getPageSize(),headlineQueryVo.getPageSize()*(headlineQueryVo.getPageNum()-1)));
        //当前翻页的页数
        pageInfo.put("pageNumber",headlineQueryVo.getPageNum());
        //每一页的尺寸，即一页多少条
        pageInfo.put("pageSize",headlineQueryVo.getPageSize());
        //总页数，即所有符合条件的新闻除以尺寸+1
        pageInfo.put("totalPages",getTotal(countSum, Long.valueOf(headlineQueryVo.getPageSize())));
        //所有符合条件的新闻条数
        pageInfo.put("totalSize",headLineNewsMapper.findCount(headlineQueryVo));
        System.out.println("mapperUsingOver");
        return pageInfo;
    }

    @Override
    public News findNewsById(int id) {
        return headLineNewsMapper.findNewsById(id);
    }

    public Long getTotal(Long countSum,Long size){
        if(countSum%size==0){
            return countSum/size;
        }
        return countSum/size+1;
    }


}
