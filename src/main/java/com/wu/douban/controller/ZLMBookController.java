package com.wu.douban.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wu.douban.entity.*;
import com.wu.douban.service.BookDetInfoService;
import com.wu.douban.service.BookInfoService;
import com.wu.douban.service.InfoService;
import com.wu.douban.service.ZLMService;
import com.wu.douban.tools.Message;
import com.wu.douban.tools.TableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/logic/book")
@Controller
@CrossOrigin
@SessionAttributes("currentUser")
public class ZLMBookController {

    @Autowired
    private ZLMService service;

    @Autowired
    private InfoService infoService;

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private BookDetInfoService bookDetInfoService;

    private Model model;

    //图书搜索
    @RequestMapping(value = "/search")
    @ResponseBody
    public TableData search(BookFocus bookFocus, int page, int limit, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        if(!currentUser.getClick().equals("")){
            bookFocus.setBookName(currentUser.getClick());
            currentUser.setClick("");
        }
        TableData data = new TableData();
        List<BookFocus> result = service.search(bookFocus, page, limit);


        QueryWrapper<Info> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",currentUser.getId());
        Info info = infoService.getOne(queryWrapper);
        String string = info.getCid();
        String[] books = string.split(",");
        for(int i = 0; i< result.size();i++){
            for(String book:books){
                if(book.equals(result.get(i).getId())){
                    result.get(i).setFocus(true);
                    break;
                }
            }
        }


        data.setCode(0);
        data.setMsg("");
        data.setCount(service.searchCount(bookFocus));
        data.setData(result);
        return data;


    }

    //通过图书id，返回对应图书信息
    @RequestMapping("/{id}")
    @ResponseBody
    public BookZLM info(@PathVariable("id") int id){
        BookZLM book = service.findBookZLMInfo(id);
        return book;
    }

    //修改图书信息
    @RequestMapping(value = "/update")
    @ResponseBody
    public Message update(BookZLM book){
        Message msg = new Message();

        msg.setError(true);
        msg.setContent("服务器错误");

        if(book!=null && book.getId() != null){
            if(service.updateBookZLMInfo(book)){
                msg.setError(false);
                msg.setContent("已经更新");
                return msg;
            }
        }
        return msg;
    }
    //评分功能
    @RequestMapping(value = "/updateScore")
    @ResponseBody
    public Message updateScore(BookZLM book){
        Message msg = new Message();

        msg.setError(true);
        msg.setContent("服务器错误");

        if(book!=null && book.getId() != null){
            BookInfo bookInfo = bookInfoService.getOne(new QueryWrapper<BookInfo>().eq("id",book.getId()));
            int sum = infoService.count(new QueryWrapper<Info>());
            if(bookInfo.getBookScore().equals("")){
                bookInfo.setBookScore(book.getBookScore());
            }else{
                double score = Double.parseDouble(bookInfo.getBookScore());
                double offset =Double.parseDouble(book.getBookScore())-score;
                double nowOffset =offset/sum;
                DecimalFormat df = new DecimalFormat("########.00");
//四舍五入
                nowOffset = Double.parseDouble(df.format(nowOffset));

                bookInfo.setBookScore(String.valueOf(score+nowOffset));
            }


            bookInfoService.updateById(bookInfo);

        }
        return msg;
    }

}
