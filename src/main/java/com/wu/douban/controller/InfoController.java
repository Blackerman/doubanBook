package com.wu.douban.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.wu.douban.entity.Article;
import com.wu.douban.entity.Info;
import com.wu.douban.entity.Message;
import com.wu.douban.entity.User;
import com.wu.douban.service.ArticleService;
import com.wu.douban.service.InfoService;
import com.wu.douban.service.MsgService;
import com.wu.douban.service.UserService;
import com.wu.douban.tools.TableData;
import com.wu.douban.tools.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/douban/info")
@CrossOrigin
@SessionAttributes("currentUser")
public class InfoController {

    @Autowired
    private InfoService infoService;

    private Model model;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private MsgService msgService;

    //添加文章
    @RequestMapping("/add")
    @ResponseBody
    public String addMyArticle(Article article, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer id = currentUser.getId();
        if(id==0){
            return "error";
        }
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        article.setDatetime(dateFormat.format(date));
        String name = currentUser.getName();
        article.setUid(id);
        article.setAuthor(name);
        articleService.save(article);
        Info info = infoService.getOne(new QueryWrapper<Info>().eq("uid",id));
        info.setAid(info.getAid()+article.getId()+",");
        infoService.updateById(info);
//        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("uid",id).eq("type",article.getType());
//        List<Article> articles = articleService.list(queryWrapper);
//        String addAid ="";
//        for(Article article1:articles){
//            addAid += article1.getId()+",";
//        }
//        QueryWrapper<Info> queryWrapper1 = new QueryWrapper<Info>().eq("uid",id);
//
//        Info info = infoService.getOne(queryWrapper1);
//        info.setAid(addAid);
//        infoService.update(info,queryWrapper1);
        return "ok";

    }

    //搜索文章
    @PostMapping("/searchMyArticle")
    @ResponseBody
    public TableData searchArticle(Article article, int page, int limit, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",uid);
        String aid = infoService.getOne(infoQueryWrapper).getAid();
        String[] strings = aid.split(",");
        TableData tableData = new TableData();
        List<Article> list = new ArrayList<Article>();
        for(String str: strings){
            QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
            articleQueryWrapper.eq("id",str);
            Article article1 = articleService.getOne(articleQueryWrapper.eq("id",str));
            if(article1==null){
                break;
            }

            if(article1.getName().contains(article.getName())
                &&article1.getTags().contains(article.getTags())){
                list.add(articleService.getOne(articleQueryWrapper.eq("id",str)));
            }

            continue;

        }

        tableData.setCode(0);
        tableData.setCount(list.size());
        tableData.setMsg("");
        tableData.setData(list);
        return tableData;
    }

    //搜索关注用户
    @PostMapping("/searchLove")
    @ResponseBody
    public TableData searchLove(User user, int page, int limit, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",uid);
        String loveId = infoService.getOne(infoQueryWrapper).getLoveId();
        TableData tableData = new TableData();
        if(loveId.equals("")){
            return tableData;
        }
        String[] strings = loveId.split(",");
        List<UserInfo> list = new ArrayList<UserInfo>();
        for(String str: strings){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            Integer id = Integer.parseInt(str);
            userQueryWrapper.eq("id",id);
            User user1 = userService.getOne(userQueryWrapper);

            QueryWrapper<Info> infoQueryWrapper1 = new QueryWrapper<>();
            infoQueryWrapper1.eq("uid",id);
            Info info = infoService.getOne(infoQueryWrapper1);

            if(user1.getName().contains(user.getName())){
                UserInfo userInfo = new UserInfo();
                userInfo.setId(user1.getId());
                userInfo.setName(user1.getName());
                userInfo.setUserLove(count(info.getLoveId()));
                userInfo.setUserLoved(count(info.getLovedId()));
                userInfo.setUserArticle(count(info.getAid()));
                userInfo.setFocus(true);
                list.add(userInfo);
            }
            continue;

        }
        tableData.setCode(0);
        tableData.setCount(list.size());
        tableData.setMsg("");
        tableData.setData(list);
        return tableData;
    }

    //搜索粉丝
    @PostMapping("/searchLoved")
    @ResponseBody
    public TableData searchLoved(User user, int page, int limit, Model model){
        //得到当前用户的id
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();

        //得到当前用户的粉丝的所有id字符串数组
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",uid);
        String lovedId = infoService.getOne(infoQueryWrapper).getLovedId();
        String[] strings = lovedId.split(",");

        //定义tabledata，用来返回数据
        TableData tableData = new TableData();
        List<UserInfo> list = new ArrayList<UserInfo>();

        //遍历每个粉丝，得到每个粉丝的info
        if(!lovedId.equals(""))
            for(String str: strings){

                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                Integer id = Integer.parseInt(str);
                userQueryWrapper.eq("id",id);
                User user1 = userService.getOne(userQueryWrapper);

                QueryWrapper<Info> infoQueryWrapper1 = new QueryWrapper<>();
                infoQueryWrapper1.eq("uid",id);
                Info info = infoService.getOne(infoQueryWrapper1);

                if(user1.getName().contains(user.getName())){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setId(user1.getId());
                    userInfo.setName(user1.getName());
                    userInfo.setUserLove(count(info.getLoveId()));
                    userInfo.setUserLoved(count(info.getLovedId()));
                    userInfo.setUserArticle(count(info.getAid()));

                    //判断用户是否关注粉丝
                    Info info1 = infoService.getOne(new QueryWrapper<Info>().eq("uid",id));
                    userInfo.setFocus(UserController.isFocus(info1.getLovedId(),String.valueOf(uid)));
                    list.add(userInfo);
                }
                continue;

            }
        tableData.setCode(0);
        tableData.setCount(list.size());
        tableData.setMsg("");
        tableData.setData(list);
        return tableData;
    }

    //搜索全部用户
    @PostMapping("/searchAll")
    @ResponseBody
    public TableData searchLoveAll(User user, int page, int limit, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer id = currentUser.getId();

        List<UserInfo> list = new ArrayList<UserInfo>();
        QueryWrapper<User> userQueryWrapper0 = new QueryWrapper<>();
        userQueryWrapper0.like("name",user.getName());
        List<User> lists = userService.list(userQueryWrapper0);
        for(User user0:lists){
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user0.getId());
            userInfo.setName(user0.getName());
            QueryWrapper<Info> infoQueryWrapper1 = new QueryWrapper<>();
            infoQueryWrapper1.eq("uid",user0.getId());
            Info info = infoService.getOne(infoQueryWrapper1);
            userInfo.setUserLove(count(info.getLoveId()));
            userInfo.setUserLoved(count(info.getLovedId()));
            userInfo.setUserArticle(count(info.getAid()));

            String str = String.valueOf(user0.getId());
            QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.eq("uid",id);
            String string = infoService.getOne(infoQueryWrapper).getLoveId();
            String[] ids = string.split(",");
            for(String idStr:ids){
                if(idStr.equals(str)){
                    userInfo.setFocus(true);
                    break;
                }
            }

            list.add(userInfo);
        }
        TableData tableData = new TableData();

        tableData.setCode(0);
        tableData.setCount(list.size());
        tableData.setMsg("");
        tableData.setData(list);
        return tableData;
    }

    //搜索推荐用户
    @PostMapping("/searchAdvise")
    @ResponseBody
    public TableData searchAdvise(User user, int page, int limit, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer id = currentUser.getId();

        String tags = currentUser.getTags();

        List<UserInfo> list = new ArrayList<UserInfo>();
        QueryWrapper<User> userQueryWrapper0 = new QueryWrapper<>();
        userQueryWrapper0.like("name",user.getName());
        List<User> preLists = userService.list(userQueryWrapper0);
        List<User> lists = new ArrayList<>();
        for(User user1:preLists){
//            Boolean flag = false;
            for(String tag: user1.getTags().split(",")){
//                if(flag)
                if(UserController.isFocus(tags,tag)){
                    lists.add(user1);
                    break;
//                    flag = true;
                }
            }
        }

        for(User user0:lists){
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user0.getId());
            userInfo.setName(user0.getName());
            QueryWrapper<Info> infoQueryWrapper1 = new QueryWrapper<>();
            infoQueryWrapper1.eq("uid",user0.getId());
            Info info = infoService.getOne(infoQueryWrapper1);
            userInfo.setUserLove(count(info.getLoveId()));
            userInfo.setUserLoved(count(info.getLovedId()));
            userInfo.setUserArticle(count(info.getAid()));

            String str = String.valueOf(user0.getId());
            QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.eq("uid",id);
            String string = infoService.getOne(infoQueryWrapper).getLoveId();
            String[] ids = string.split(",");
            for(String idStr:ids){
                if(idStr.equals(str)){
                    userInfo.setFocus(true);
                    break;
                }
            }

            list.add(userInfo);
        }
        TableData tableData = new TableData();

        tableData.setCode(0);
        tableData.setCount(list.size());
        tableData.setMsg("");
        tableData.setData(list);
        return tableData;
    }

    //关注功能实现
    @PostMapping("/focusLove")
    @ResponseBody
    public String clickFocus(User user, Boolean focus, Model model){
        String result;
        //获得当前用户ID
        User currentUser = (User) model.getAttribute("currentUser");
        Integer id = currentUser.getId();
        //获得目标用户ID，并将其转化为String
        Integer focusId = user.getId();
        String stringId = String.valueOf(focusId)+",";
        //找到当前用户关注列表
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",id);
        Info info = infoService.getOne(infoQueryWrapper);
        Info focusLove = infoService.getOne(new QueryWrapper<Info>().eq("uid",focusId));
        //根据focus状态判断用户是否被关注
        if(focus){
            //取消关注用户
            info.setLoveId(UserController.strReplace(info.getLoveId(),String.valueOf(focusId)));
            focusLove.setLovedId(UserController.strReplace(focusLove.getLovedId(),String.valueOf(id)));
            result="cancel";
        }else{
            //关注用户
            info.setLoveId(info.getLoveId()+stringId);
            focusLove.setLovedId(focusLove.getLovedId()+String.valueOf(id)+",");
            result="focus";
            //添加消息
            Message message = new Message();
            message.setUid(id);
            message.setRid(focusId);
            message.setType("focus");
            message.setContent("很高兴认识你");
            msgService.save(message);
        }
        infoService.updateById(focusLove);
        infoService.update(info,infoQueryWrapper);
        return result;
    }




    //统计字符串数目
    private int count(String string){

        if(string.equals(""))
            return 0;
        return string.split(",").length;
    }



}
