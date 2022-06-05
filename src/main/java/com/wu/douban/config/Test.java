package com.wu.douban.config;

public class Test {
    public static void main(String[] args) {
        String str = "|dd|ss|123";
        String replaceStr = str.replace("|","!");
        replaceStr = str.replaceFirst("[|]","!");
        System.out.println(replaceStr);
    }
}
