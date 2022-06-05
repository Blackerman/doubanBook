package com.wu.douban.controller;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.wu.douban.entity.WeishanchaoTv;
import com.wu.douban.entity.*;
//import com.wu.douban.entity.T4;
//import com.wu.douban.service.WeishanchaoTVService;
import com.wu.douban.service.*;
//import com.wu.douban.service.T4Service;
import com.wu.douban.tools.TableData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
@RequestMapping("/douban/collection")
@CrossOrigin
@SessionAttributes("currentUser")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private ZLMService zlmService;

    @Autowired
    private BookDetInfoService bookDetInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private InfoService infoService;

    @Autowired
    private BookInfoService bookInfoService;


    @PostMapping("/addCollection")
    public String addCollection(@Param("id") String id,Boolean focus, Model model){
        String result;
        User currentUser = (User) model.getAttribute("currentUser");
        Info info = infoService.getOne(new QueryWrapper<Info>().eq("uid",currentUser.getId()));
        BookInfo bookInfo = bookInfoService.getOne(new QueryWrapper<BookInfo>().eq("id",id));
        BookDetInfo bookDetInfo = bookDetInfoService.getOne(new QueryWrapper<BookDetInfo>().eq("id",id));
        if(focus){
            info.setCid(info.getCid().replace(id+",",""));
            result="取消收藏";
            String preTags = currentUser.getTags();
            preTags = preTags.replaceFirst(bookInfo.getBookAuthor()+",","");
            currentUser.setTags(preTags);
        }else{
            info.setCid(info.getCid()+id+",");
            result="收藏成功！";

            //将收藏图书的标签绑定用户
//            String tags = bookInfo.getBookName()+","+ bookInfo.getBookAuthor()+",";
//            currentUser.setTags(currentUser.getTags()+tags);
            //将收藏图书的标签绑定用户
            String tags = bookInfo.getBookAuthor()+",";
            if(bookDetInfo!=null){
                String[] anotherLike = bookDetInfo.getBookDAnotherlike().split("[|]");
                for(String tag:anotherLike){
                    if(!tag.equals(""))
                        tags =tags+tag+",";
                }
            }

            currentUser.setTags(currentUser.getTags()+tags);
        }

        userService.update(currentUser,new QueryWrapper<User>().eq("id",currentUser.getId()));
        infoService.updateById(info);
        return result;
//
//        Integer id1 = currentUser.getId();
//        Collection collection = new Collection();
//        collection.setUid(id1);
//        collection.setCid(id);
//        collection.setDel(0);
//        collection.setType(1);
//        if (collectionService.save(collection)){
//            return "ok";
//        }
//        return "error";
    }

    @PostMapping({"/search"})
    @ResponseBody
    public TableData search( BookFocus bookFocus0, Model model, int page, int limit) {
        User currentUser = (User)model.getAttribute("currentUser");
        Integer id = currentUser.getId();
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",id);
        Info info = infoService.getOne(infoQueryWrapper);
        TableData tableData = new TableData();
        if(info.getCid().equals("")){
            return tableData;
        }
        String[] strings = info.getCid().split(",");
        List<BookFocus> bookFocusList = new ArrayList<>();
        for(String str:strings){
            int cid = Integer.valueOf(str);
            BookFocus bookFocus = this.zlmService.findBookZLMInfo(cid);
            bookFocus.setFocus(true);
            if(bookFocus.getBookName().contains(bookFocus0.getBookName())&&bookFocus.getBookAuthor().contains(bookFocus0.getBookAuthor()))
                bookFocusList.add(bookFocus);

        }
        tableData.setCode(0);
        tableData.setCount(bookFocusList.size());
        tableData.setMsg("");
        tableData.setData(bookFocusList);
        return tableData;


//        List<BookZLM> books = new ArrayList();
//        Page<Collection> page1 = new Page((long)page, (long)limit);
//        TableData tableData = new TableData();
//        int del = 0;
//        this.collectionService.page(page1, (Wrapper)((QueryWrapper)((QueryWrapper)(new QueryWrapper()).eq("type", type)).eq("del", Integer.valueOf(del))).eq("uid", id));
//        tableData.setCode(0);
//        tableData.setCount(page1.getTotal());
//        tableData.setMsg("");
//        Iterator var11 = page1.getRecords().iterator();
//
//        while(var11.hasNext()) {
//            Collection record = (Collection)var11.next();
//            Integer cid = record.getCid();
//            BookZLM book = this.zlmService.findBookZLMInfo(cid);
//            books.add(book);
//        }
//
//        tableData.setData(books);
//        return tableData;
    }

    public String splitStr(String strs){
        String str="";
        String[] strings= strs.split("|");

        for(String temp:strings){
            str= str + temp + ",";
        }

        return str;
    }








}
