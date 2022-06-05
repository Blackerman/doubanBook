package com.wu.douban.tools;

import lombok.Data;

import java.util.List;

@Data
public class TableData {
    private int code;
    private String msg;
    private long count;
    private List<? extends Object> data;
}
