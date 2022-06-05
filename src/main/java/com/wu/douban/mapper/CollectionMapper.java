package com.wu.douban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wu.douban.entity.Collection;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Mapper
@Component
public interface CollectionMapper extends BaseMapper<Collection> {
}
