package com.wu.douban.tools;

import lombok.Data;

@Data
public class ArticleInfo {

    private Integer id;

    private String name="";

    private String author="";

    private Integer uid=0;

    private String content="";

    private Integer review=0;

    private Integer type=0;

    private String datetime= "";

    private String tags="其他";

    private Boolean love=false;
    private Boolean focus=false;

    private Integer aid=0;
}
