package com.wu.douban.tools;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MsgInfo {

    private Integer mid;

    private Integer uid=0;
    private Integer rid=0;


    private String name="";
    private String content="";
    private String msgTime="";
    private String state="";
    private String type="";

}
