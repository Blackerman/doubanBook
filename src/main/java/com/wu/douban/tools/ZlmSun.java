package com.wu.douban.tools;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ZlmSun {
    public String name ;
    public List<ZlmChild> children = new ArrayList<>();

//    private String name;
//    private int id;
//    private String sex;
//
//    public ZlmSun(String name, int id, String sex){
//        this.name = name;
//        this.id = id;
//        this.sex = sex;
//    }

}
