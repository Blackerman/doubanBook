package com.wu.douban.tools;

import java.util.ArrayList;
import java.util.List;

public class ZlmChild {
    public String name;
    public int value;
    public List<ZlmChild> children = new ArrayList<>();

    public  ZlmChild(String name, int value){
        this.name = name;
        this.value = value;
    }
    public ZlmChild(){

    }

}
