package com.wu.douban.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Message {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer mid;

    private Integer uid=0;
    private Integer rid=0;


    private String content="";
    private LocalDateTime msgTime=LocalDateTime.now();
    private boolean isRead=false;
    private String type="";


}
