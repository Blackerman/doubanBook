package com.wu.douban.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.sun.deploy.util.SessionState;
import com.wu.douban.entity.Article;
import com.wu.douban.entity.CurrentUser;
import com.wu.douban.entity.Info;
import com.wu.douban.entity.User;
import com.wu.douban.service.ArticleService;
import com.wu.douban.service.InfoService;
import com.wu.douban.service.UserService;
import com.wu.douban.tools.TableData;
//import jdk.nashorn.internal.objects.NativeUint16Array;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author team04
 * @since 2021-11-29
 */
@Controller
@RequestMapping("/douban/user")
@CrossOrigin
@SessionAttributes("currentUser")
@SuppressWarnings("all")
public class UserController {

    @Autowired
    private UserService UserService;
    private Model model;

    @Autowired
    private InfoService infoService;

    @Autowired
    private ArticleService articleService;



    @PostMapping("/addUser")
    @ResponseBody
    public com.wu.douban.entity.User addUser(User user){
        String name = user.getName();
        User name1 = UserService.getOne(new QueryWrapper<User>().eq("name", name));
        if (name1!=null){
            return null;
        }
        if (UserService.save(user)){

            //添加新注册用户到info表
            name1 = UserService.getOne(new QueryWrapper<User>().eq("name",name));
            Info info = new Info();
            info.setUid(name1.getId());
            infoService.save(info);
            return user;
        }
        return null;
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public User info(@PathVariable("id") int id){
        return UserService.getById(id);
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@Param("name")String name, @Param("password") String password, Model model){
        User name1 = UserService.getOne(new QueryWrapper<User>().eq("name", name));
        if (name1 == null){
            return "l1";
        }
        String password1 = name1.getPassword();
        int state = name1.getState();
        if (!password.equals(password1)){
            return "pswd error";
        }
        if (password.equals(password1)&&state==1){

            return "ok";
        }
        if(password.equals(password1)&&state==0){
            return "q0";
        }
        if(password.equals(password1)&&state==2){
            return "q2";
        }
        else {
            return "l2";
        }
    }

    @PostMapping("/login2")
    public String login2(@Param("name")String name, @Param("password") String password, Model model){
        User name1 = UserService.getOne(new QueryWrapper<User>().eq("name", name));
        if (name1 == null){
            return "l1";
        }
        String password1 = name1.getPassword();
        int state = name1.getState();
        if (password.equals(password1)){
            model.addAttribute("currentUser",name1);
            return "redirect:/html/mainpage.html";
        }
        else {
            return "l2";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession httpSession, SessionStatus sessionStatus){
        httpSession.invalidate();
        sessionStatus.setComplete();
        return "redirect:/html/index.html";
    }

    @RequestMapping("/currentUser")
    @ResponseBody
    public User currentUser(Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        if (currentUser!=null){
            return currentUser;
        }
        return null;
    }


    //搜索用户
    @PostMapping("/search")
    @ResponseBody
    public TableData Search(User user, int page, int limit){
        int state = user.getState();
        QueryWrapper<com.wu.douban.entity.User> userQueryWrapper = new QueryWrapper<>();
        Page<User> page1=new Page<>(page,limit);
        TableData tableData = new TableData();
        String name = user.getName();
        if (!StringUtils.isEmpty(name)){
            userQueryWrapper.like("name",name);
            List<com.wu.douban.entity.User> result = UserService.list(userQueryWrapper);
            tableData.setCode(0);
            tableData.setCount(result.size());
            tableData.setMsg("");
            tableData.setData(result);
            return tableData;
        }
        if (state!=-1){
            userQueryWrapper.eq("state",state);
        }
        UserService.page(page1,userQueryWrapper);
        tableData.setCode(0);
        tableData.setCount(page1.getTotal());
        tableData.setMsg("");
        tableData.setData(page1.getRecords());
        return tableData;
    }

    //用户权限修改
    @RequestMapping("/update")
    @ResponseBody
    public User update(User user){
        if (user!=null&&user.getId()!=0){
            User byId = UserService.getById(user.getId());
            if (byId!=null){
                byId.setState(user.getState());
                UserService.update(byId,new QueryWrapper<User>().eq("id",byId.getId()));
                return byId;
            }
        }
        return null;
    }

    //注销账户
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(User user,Model model){
        User currentUser = (User) model.getAttribute("currentUser");
        if (user!=null){
            if(currentUser.getName().equals(user.getName())&&currentUser.getPassword().equals(user.getPassword())){

                //更新Info表的关注用户和粉丝
                Info info = infoService.getOne(new QueryWrapper<Info>().eq("uid",currentUser.getId()));
                String[] loveId = info.getLoveId().split(",");
                String[] lovedId = info.getLovedId().split(",");
                //得到每个关注用户列表，更新每个关注用户的粉丝信息
                if(!loveId[0].equals(""))
                    for(String str:loveId) {
                        int id = Integer.valueOf(str);
                        Info loveInfo = infoService.getOne(new QueryWrapper<Info>().eq("uid", id));
                        String replacedStr =String.valueOf(currentUser.getId());
                        String finalLoveIds = strReplace(loveInfo.getLovedId(),replacedStr);
                        loveInfo.setLovedId(finalLoveIds);
                        infoService.updateById(loveInfo);
                    }
                //得到每个粉丝列表，更新每个粉丝的关注信息
                if(!lovedId[0].equals(""))
                    for(String str:lovedId){
                        int id = Integer.valueOf(str);
                        Info lovedInfo = infoService.getOne(new QueryWrapper<Info>().eq("uid", id));
                        String replacedStr =String.valueOf(currentUser.getId());
                        String finalLoveIds = strReplace(lovedInfo.getLoveId(),replacedStr);
                        lovedInfo.setLoveId(finalLoveIds);
                        infoService.updateById(lovedInfo);
                    }
//                更新Article表
                String[] articles = info.getAid().split(",");
                for(String articleId:articles){
                    articleService.remove(new QueryWrapper<Article>().eq("id",articleId));
                }

                infoService.removeById(info);
                UserService.removeById(currentUser);
                return "ok";
            }else{
                return "error";
            }

        }
        return null;
    }

    //修改密码
    @PostMapping("/modifyPsw")
    @ResponseBody
    public String modifyPsw(String p1, String p2, String p3, Model model){
        com.wu.douban.entity.User currentUser = (com.wu.douban.entity.User) model.getAttribute("currentUser");
        String password = currentUser.getPassword();
        if (!p1.equals(password)){
            return "error1";
        }
        if (!p2.equals(p3)){
            return "error2";
        }
        currentUser.setPassword(p2);
        if (UserService.update(currentUser,new QueryWrapper<com.wu.douban.entity.User>().eq("id",currentUser.getId()))){
            return "ok";
        }
       return null;
    }

    static public String strReplace(String startStr,String replacedStr){
        String[] strings = startStr.split(",");
        List<String> stringList = new ArrayList<>();
        for(String str : strings){
            if(!str.equals(replacedStr)){
                stringList.add(str);
            }
        }
        String finalLoveIds="";
        for(int i =0; i<stringList.size();i++){
            finalLoveIds += stringList.get(i) + ",";
        }
        return  finalLoveIds;

    }

    static public boolean isFocus(String startStr, String str){
        String[] strings = startStr.split(",");
        for(String string : strings){
            if(str.equals(string)){
                return true;
            }
        }
        return  false;
    }



}

