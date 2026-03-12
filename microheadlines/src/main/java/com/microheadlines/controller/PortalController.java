package com.microheadlines.controller;

import com.microheadlines.common.Result;
import com.microheadlines.common.ResultCodeEnum;
import com.microheadlines.entity.News;
import com.microheadlines.entity.vo.HeadlineQueryVo;
import com.microheadlines.service.impl.HeadlineServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("portal")

public class PortalController {
    @Autowired
    HeadlineServiceImpl headLineService;
    @PostMapping("findNews")
    protected Result findNewsPage(@RequestBody HeadlineQueryVo headlineQueryVo){
        Map data=new HashMap<>();
        System.out.println("controllerStart");
        data.put("pageInfo",headLineService.findPage(headlineQueryVo));
        return Result.build(data, ResultCodeEnum.SUCCESS);
    }

    @GetMapping("detail")
    protected Result findNewsPage(int id){
        News newsById = headLineService.findNewsById(id);
        return Result.ok(newsById);
    }
}
