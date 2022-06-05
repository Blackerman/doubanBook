package com.wu.douban.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wu.douban.entity.Article;
import com.wu.douban.entity.Message;
import com.wu.douban.entity.User;
import com.wu.douban.service.InfoService;
import com.wu.douban.service.MsgService;
import com.wu.douban.service.UserService;
import com.wu.douban.tools.MsgInfo;
import com.wu.douban.tools.TableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/douban/message")
@CrossOrigin
@SessionAttributes("currentUser")
public class MsgController {

    @Autowired
    private MsgService msgService;

    @Autowired
    private UserService userService;

    private Model model;

    //发送信息
    @PostMapping("/msg")
    @ResponseBody
    public String msgSend(Integer id, Message message, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();
        message.setUid(uid);
        message.setRid(id);
        message.setType("msg");
        if(msgService.save(message)){
            return "success";
        }
        return "false";
    }

    //删除信息
    @PostMapping("/msgDelete")
    @ResponseBody
    public String msgDelete(Message message){
        msgService.remove(new QueryWrapper<Message>().eq("id",message.getMid()));
        return "ok";
    }

    //阅读信息
    @RequestMapping("/msgRead")
    @ResponseBody
    public String  readMsg(Integer mid) {
        Message message = msgService.getOne(new QueryWrapper<Message>().eq("id",mid));
        message.setRead(true);
        msgService.updateById(message);
        return "ok";
    }

    //通知栏消息
    @PostMapping("/unreadMsg")
    @ResponseBody
    public List<Integer> msgCount(Model model){
        List<Integer> list = new ArrayList<>();
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();
        //统计未读消息数量
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rid",uid).eq("is_read",0).eq("type","msg");
        int msgSum = msgService.count(queryWrapper);
        list.add(msgSum);

        //统计新粉丝数量
        QueryWrapper<Message> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("rid",uid).eq("is_read",0).eq("type","focus");
        int focusSum = msgService.count(queryWrapper1);
        list.add(focusSum);

        //统计文章点赞数量
        QueryWrapper<Message> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("rid",uid).eq("is_read",0).eq("type","love");
        int loveSum = msgService.count(queryWrapper2);
        list.add(loveSum);

        return list;
    }

    //信息搜索
    @PostMapping("/msgSearch")
    @ResponseBody
    public TableData msgSearch( String type, String state, Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        Integer uid = currentUser.getId();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        TableData tableData = new TableData();

        queryWrapper.eq("rid",uid).like("is_read",state).like("type",type);
        List<Message> messages = msgService.list(queryWrapper);
        List<MsgInfo> msgInfos = new ArrayList<>();
        for(Message message: messages){
            Integer id = message.getUid();
            MsgInfo msgInfo = new MsgInfo();
            msgInfo.setName(userService.getOne(new QueryWrapper<User>().eq("id",id)).getName());
            if(message.getType().equals("msg")){
                msgInfo.setType("私信");
            }else if(message.getType().equals("focus")){
                msgInfo.setType("关注");
            }else{
                msgInfo.setType("点赞");
            }
            if(message.isRead()){
                msgInfo.setState("已读");
            }else{
                msgInfo.setState("未读");
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            msgInfo.setMsgTime(df.format(message.getMsgTime()));
            msgInfo.setContent(message.getContent());
            msgInfo.setUid(message.getUid());
            msgInfo.setMid(message.getMid());

            msgInfos.add(msgInfo);

        }


        tableData.setCode(0);
        tableData.setCount(msgInfos.size());
        tableData.setMsg("");
        tableData.setData(msgInfos);
        return tableData;

    }


}
