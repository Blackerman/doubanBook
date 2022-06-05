package com.wu.douban.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.douban.entity.*;
import com.wu.douban.service.ArticleService;
import com.wu.douban.service.InfoService;
import com.wu.douban.service.MsgService;
import com.wu.douban.service.UserService;
import com.wu.douban.tools.ArticleInfo;
import com.wu.douban.tools.TableData;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/logic/article")
@CrossOrigin
@SessionAttributes("currentUser")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    private InfoService infoService;

    @Autowired
    private MsgService msgService;



    //添加公告
    @RequestMapping("/add")
    @ResponseBody
    public String addArticle(Article article, Model model){
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
        return "ok";

    }

    //获得文章/公告id
    @RequestMapping("/{id}")
    @ResponseBody
    public Article info(@PathVariable("id") int id){

        return articleService.getById(id);
    }

    //搜索文章公告
    @PostMapping("/search")
    @ResponseBody
    public TableData Search(Article article, int page, int limit){
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        Page<Article> page1=new Page<>(page,limit);
        TableData tableData = new TableData();
        String name = article.getName();
        int type = article.getType();
        articleQueryWrapper.eq("type",type);
        articleQueryWrapper.like("name",name).like("author",article.getAuthor()).like("tags",article.getTags());
        articleService.page(page1,articleQueryWrapper);
        tableData.setCode(0);
        tableData.setCount(page1.getTotal());
        tableData.setMsg("");
        tableData.setData(page1.getRecords());
        return tableData;
    }

    //文章列表搜索
    @PostMapping("/searchLove")
    @ResponseBody
    public TableData SearchLove(Article article, int page, int limit, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer id = currentUser.getId();
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",id);
        Info info = infoService.getOne(infoQueryWrapper);

        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        Page<Article> page1=new Page<>(page,limit);
        TableData tableData = new TableData();
        String name = article.getName();
        int type = article.getType();
        articleQueryWrapper.eq("type",type);
        articleQueryWrapper.like("name",name).like("author",article.getAuthor()).like("tags",article.getTags());
        List<Article> articleList = articleService.list(articleQueryWrapper);
        List<ArticleInfo> articleInfoList = new ArrayList<>();
        for(Article article1:articleList){
            ArticleInfo articleInfo = new ArticleInfo();

            //判断当前用户是否喜欢该文章
            if(UserController.isFocus(info.getLoveArticleId(),String.valueOf(article1.getId()))){
                articleInfo.setLove(true);
            }
            //得到文章作者信息
            User user = userService.getOne(new QueryWrapper<User>().eq("name",article1.getAuthor()));
            //判断当前用户是否关注文章作者
            if(UserController.isFocus(info.getLoveId(),String.valueOf(user.getId()))){
                articleInfo.setFocus(true);
            }

            articleInfo.setAid(user.getId());
            articleInfo.setId(article1.getId());
            articleInfo.setName(article1.getName());
            articleInfo.setAuthor(article1.getAuthor());
            articleInfo.setReview(article1.getReview());
            articleInfo.setTags(article1.getTags());
            articleInfo.setDatetime(article1.getDatetime());
            articleInfo.setContent(article1.getContent());
            articleInfoList.add(articleInfo);
        }

        tableData.setCode(0);
        tableData.setCount(articleInfoList.size());
        tableData.setMsg("");
        tableData.setData(articleInfoList);
        return tableData;
    }

    //删除文章/公告
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(Article article,Model model){
        if (article.getId()!=null){
            //更新info表信息
            User currentUser = (User) model.getAttribute("currentUser");
            if(article.getType()==1){
                Integer uid = articleService.getOne(new QueryWrapper<Article>().eq("id",article.getId())).getUid();
                Info info = infoService.getOne(new QueryWrapper<Info>().eq("uid",uid));
                String nowArticle = UserController.strReplace(info.getAid(),String.valueOf(article.getId()));
                info.setAid(nowArticle);
                infoService.updateById(info);
            }

            articleService.removeById(article);
            return "ok";

        }
        return null;
    }

    //更新文章/公告
    @RequestMapping("/update")
    @ResponseBody
    public Article update(Article article){
        if (article.getId()!=0){
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Article byId = articleService.getById(article.getId());
            byId.setName(article.getName());
            byId.setContent(article.getContent());
            byId.setTags(article.getTags());
            byId.setDatetime(dateFormat.format(date));

            articleService.update(byId,new QueryWrapper<Article>().eq("id",byId.getId()));
            return byId;
        }
        return null;
    }

    //文章点赞功能
    @RequestMapping("/love")
    @ResponseBody
    public String love(Article article, Boolean love, Model model){
        String result="error";
        if (article.getId()!=0){
            //找到当前用户的信息
            User currentUser = (User) model.getAttribute("currentUser");
            Integer id = currentUser.getId();
            QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
            infoQueryWrapper.eq("uid",id);
            Info info = infoService.getOne(infoQueryWrapper);

            //根据love状态判断文章是否被用户收藏
            int offset=0;
            if(love){
                //取消收藏文章
                info.setLoveArticleId(UserController.strReplace(info.getLoveArticleId(),String.valueOf(article.getId())));
                offset--;
                result="cancel";
            }else{
                //收藏文章
                info.setLoveArticleId(info.getLoveArticleId()+article.getId()+",");
                offset++;
                result="love";

                //发送信息
                User user = userService.getOne(new QueryWrapper<User>().eq("name",article.getAuthor()));
                Message message = new Message();
                message.setUid(id);
                message.setRid(user.getId());
                message.setType("love");
                message.setContent("你的文章很棒！");
                msgService.save(message);
            }
            infoService.update(info,infoQueryWrapper);

            Article byId = articleService.getById(article.getId());
            byId.setReview(byId.getReview()+offset);

            articleService.update(byId,new QueryWrapper<Article>().eq("id",byId.getId()));
            return result;
        }
        return result;
    }

    //公告通知功能
    @RequestMapping("/topNotice")
    @ResponseBody
    public TableData topNotice(Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        String id = String.valueOf(currentUser.getId());


        TableData tableData = new TableData();
        List<Article> articleList = new ArrayList<>();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        articleList = articleService.list(queryWrapper.eq("type",2));
        List<ArticleInfo> unReadList = new ArrayList<>();
        QueryWrapper<Info> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("uid",id);
        Info info = infoService.getOne(infoQueryWrapper);
        for(Article article1: articleList){
            if(!UserController.isFocus(article1.getTags(),id)){

                ArticleInfo articleInfo = new ArticleInfo();

                //得到文章作者信息
                User user = userService.getOne(new QueryWrapper<User>().eq("name",article1.getAuthor()));
                //判断当前用户是否关注文章作者
                if(UserController.isFocus(info.getLoveId(),String.valueOf(user.getId()))){
                    articleInfo.setFocus(true);
                }

                articleInfo.setAid(user.getId());
                articleInfo.setId(article1.getId());
                articleInfo.setName(article1.getName());
                articleInfo.setAuthor(article1.getAuthor());
                articleInfo.setReview(article1.getReview());
                articleInfo.setTags(article1.getTags());
                articleInfo.setDatetime(article1.getDatetime());
                articleInfo.setContent(article1.getContent());
                unReadList.add(articleInfo);
            }

        }

        tableData.setCode(0);
        tableData.setCount(unReadList.size());
        tableData.setMsg("");
        tableData.setData(unReadList);


        return tableData;
    }

    //公告阅读
    @RequestMapping("/readNotice")
    @ResponseBody
    public String  readNotice(Article article, Model model) {
        User currentUser = (User) model.getAttribute("currentUser");
        String id = String.valueOf(currentUser.getId());
        Article article1 = articleService.getOne(new QueryWrapper<Article>().eq("id",article.getId()));

        if(!UserController.isFocus(article1.getTags(),id)){
            article1.setTags(article1.getTags()+id+",");
            article1.setReview(article1.getReview()+1);
            articleService.updateById(article1);
        }


        return "";
    }

}
