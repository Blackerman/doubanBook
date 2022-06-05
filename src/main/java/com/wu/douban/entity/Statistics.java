package com.wu.douban.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Statistics {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private int book=0;
    private int article=0;
    private int notice=0;
    private int user=0;
    private int admin=0;
    private int month=0;
    private int week=0;
    private int day=0;
    private int year=0;
}
