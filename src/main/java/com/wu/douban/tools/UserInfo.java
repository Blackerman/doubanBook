package com.wu.douban.tools;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id=0;
    private String name="";
    private int collect=0;
    private int userLove=0;
    private int userLoved=0;
    private int userArticle=0;
    private int message=0;
    private boolean focus=false;

}
