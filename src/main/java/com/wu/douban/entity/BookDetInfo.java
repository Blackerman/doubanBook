package com.wu.douban.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BookDetInfo {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    private String id;

    private String bookDName;
    private String bookDAuthor;
    private String bookDFive;
    private String bookDFour;
    private String bookDThree;
    private String bookDTwo;
    private String bookDOne;
    private String bookDInfo;
    private String bookDTags;
    private String bookDAnotherlike;
}
