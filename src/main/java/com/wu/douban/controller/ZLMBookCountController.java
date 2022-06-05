package com.wu.douban.controller;


import com.alibaba.fastjson.JSON;
import com.wu.douban.entity.BookReview;
import com.wu.douban.entity.BookScoreCount;
import com.wu.douban.service.ZLMService;
import com.wu.douban.tools.ZlmChild;
import com.wu.douban.tools.ZlmSun;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
//import org.springframework.web.context.annotation.RequestScope;

@RestController
@RequestMapping("/logic/book/count")
public class ZLMBookCountController {

    @Autowired
    private ZLMService service;

    @RequestMapping(value = "/scorecount")
    public List<BookScoreCount> bookScoreCount(){

        return service.scoreCount();
    }

    @RequestMapping(value = "/suncount")
    @ResponseBody
    public List<ZlmSun> bookSunCount(){
        List<BookScoreCount> list = service.rankCount();
        BookReview review = service.reviewCount();
        List<ZlmSun> suns = service.sunMain(list, review);
//        String json = JSON.toJSONString(suns);
//        return json;
        return suns;

    }

    @RequestMapping(value = "/rankcount")
    public int[] rankCount(){
        List<BookScoreCount> list = service.rankCount();
        int[] percent = service.rankMain(list);

        return percent;
    }

    @RequestMapping(value = "/hotauthor")
    public List<BookScoreCount> hotAuthor(){
        List<BookScoreCount> list = service.hotAuthor();
        return list;
    }

}
