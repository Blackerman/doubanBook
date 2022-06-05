package com.wu.douban.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wu.douban.entity.*;
import com.wu.douban.service.*;
import com.wu.douban.tools.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/douban/statistics")
@CrossOrigin
@SessionAttributes("currentUser")
public class StatisticsController {
    @Autowired
    private UserService userService;

    @Autowired
    private InfoService infoService;

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private StaService staService;

    @Autowired
    private BookDetInfoService bookDetInfoService;

    @Autowired
    private MsgService msgService;

    private Model model;

    //轮播图，carousel
    @PostMapping("/carousel")
    @ResponseBody
    public List<BookInfo> carousel(Model model){
        List<BookInfo> list0 = new ArrayList<>();

        List<BookInfo> list = new ArrayList<>();
        User currentUser = (User) model.getAttribute("currentUser");
        String [] tags = currentUser.getTags().split(",");
        for(String tag : tags){
            QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("book_name",tag).or().like("book_author",tag);
            list.addAll(bookInfoService.list(queryWrapper));
        }
        int x = list.size();
        int[] num = randNum(x);

        String[] ids = {"1007305","1012611","1045818","1043815", "10583099" };

        for(int i=0;i<num.length;i++){
            if(num[i]==0&&i>0){
                BookInfo bookInfo = bookInfoService.getOne(new QueryWrapper<BookInfo>().eq("id",ids[i]));
//                BookInfo bookInfo = bookInfoService.getOne(new QueryWrapper<BookInfo>().like("id",""));
                list0.add(bookInfo);
            }else{
                list0.add(list.get(num[i]));
            }

        }

        return list0;

    }

    //个人信息信息
    @PostMapping("/personal")
    @ResponseBody
    public UserInfo perMsg(Model model){
        UserInfo userInfo = new UserInfo();
        //得到当前用户的id
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();
        userInfo.setId(uid);

        //得到用户的消息
        int msgs = msgService.count(new QueryWrapper<Message>().eq("rid",uid));
        userInfo.setMessage(msgs);

        //得到当前用户的名字
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",uid);
        User user = userService.getOne(queryWrapper);
        userInfo.setName(user.getName());

        //得到当前用户的info
        QueryWrapper<Info> infoQueryWrapper1 = new QueryWrapper<>();
        infoQueryWrapper1.eq("uid",uid);
        Info info = infoService.getOne(infoQueryWrapper1);
        userInfo.setCollect(count(info.getCid()));
        userInfo.setUserLove(count(info.getLoveId()));
        userInfo.setUserLoved(count(info.getLovedId()));
        userInfo.setUserArticle(count(info.getAid()));



        return userInfo;
    }

    //平台信息
    @PostMapping("/platform")
    @ResponseBody
    public List<Statistics> platformMsg(){
        List<Statistics> list = new ArrayList<>();

        //统计文章总数
        Statistics today = countInfo();
        list.add(today);
        int weekNow = today.getWeek();

        Statistics yesterday = searchInfo(-1);
        list.add(yesterday);

        Statistics week = searchInfo(0-weekNow);
        list.add(week);

        Statistics month = searchInfo(0-today.getDay());
        list.add(month);

        return list;
    }

    //折线图
    @PostMapping("/line")
    @ResponseBody
    public List<Statistics> lineChart(){
        List<Statistics> list = new ArrayList<>();
        for(int i = -29;i<=0;i++){
            list.add(searchInfo(i));
        }
        return list;
    }

    @PostMapping("/clickURL")
    @ResponseBody
    public String clickUrl(String str, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        currentUser.setClick(str);

        return "ok";
    }


    //统计今日信息
    private Statistics countInfo(){
        Statistics sum = new Statistics();
        sum.setBook(bookInfoService.count(new QueryWrapper<BookInfo>()));
        sum.setArticle(articleService.count(new QueryWrapper<Article>().eq("type",1)));
        sum.setNotice(articleService.count(new QueryWrapper<Article>().eq("type",2)));
        sum.setUser(userService.count(new QueryWrapper<User>().eq("state",0)));
        sum.setAdmin(userService.count(new QueryWrapper<User>().eq("state",1)));
        Calendar calendar = Calendar.getInstance();
        sum.setYear(calendar.get(Calendar.YEAR));
        sum.setMonth(calendar.get(Calendar.MONTH)+1);
        sum.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        int week = (calendar.get(Calendar.DAY_OF_WEEK)+6)%7;
        if(week==0)
            week = 7;
        sum.setWeek(week);

        QueryWrapper<Statistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year",sum.getYear()).eq("Month",sum.getMonth()).eq("day",sum.getDay());
        if(staService.getOne(queryWrapper)!=null){
            staService.update(sum,queryWrapper);
        }else{
            staService.save(sum);
        }


        return sum;
    }

    //查询过去信息
    private Statistics searchInfo(int offset){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
        Date date = calendar.getTime();
        int year = date.getYear()+1900;
        int month = date.getMonth()+1;
        int day = date.getDate();
        QueryWrapper<Statistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year",year).eq("Month",month).eq("day",day);
        Statistics statistics = staService.getOne(queryWrapper);
//        statistics.setBook(countInfo().getBook()-statistics.getBook());
//        statistics.setArticle(countInfo().getArticle()-statistics.getArticle());
//        statistics.setNotice(countInfo().getNotice()-statistics.getNotice());
//        statistics.setUser(countInfo().getUser()-statistics.getUser());
//        statistics.setAdmin(countInfo().getAdmin()-statistics.getAdmin());
        return statistics;


    }

    //统计字符串数目
    private int count(String string){

        if(string.equals(""))
            return 0;
        return string.split(",").length;
    }

    //得到一个随机数组
    private int[] randNum(int x){
        int[] num = {0,0,0,0,0};
        Random rand = new Random();
        if(x<=5){
            for(int i = 0; i < x;i++){
                num[i] = i;
            }
        }else{
            for(int i=0;i<5;){
                int temp = rand.nextInt(x);
                if(!isArray(temp,num)){
                    num[i]= temp;
                    i++;
                }
            }
        }

        return num;
    }

    //判断一个数字是否在数组中
    private boolean isArray(int x,int[] numbers){
        for(int number:numbers){
            if(x==number)
                return true;
        }
        return false;
    }
}
