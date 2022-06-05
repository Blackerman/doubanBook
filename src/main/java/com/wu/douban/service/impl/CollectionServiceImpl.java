package com.wu.douban.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.douban.entity.Collection;
import com.wu.douban.mapper.CollectionMapper;
import com.wu.douban.service.CollectionService;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {
    public CollectionServiceImpl() {
    }
}
