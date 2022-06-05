package com.wu.douban.entity;

import com.wu.douban.tools.TableData;
import lombok.Data;

@Data
public class CurrentUser extends User {
    private Integer id;

    private String name;

    private String password;

    private int state;

    private String click="";


    public User setName(String name) {
        this.name = name;
        return null;
    }

    public User setId(Integer id) {
        this.id = id;
        return null;
    }

    public User setPassword(String password) {
        this.password = password;
        return null;
    }

    public User setState(int state) {
        this.state = state;
        return null;
    }

    public User setClick(String str){
        this.click = str;
        return null;
    }

    public int getState() {
        return state;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getClick(){return click;}


}
