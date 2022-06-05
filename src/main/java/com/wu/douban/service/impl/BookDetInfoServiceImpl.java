package com.wu.douban.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.douban.entity.BookDetInfo;
import com.wu.douban.entity.BookInfo;
import com.wu.douban.mapper.BookDetInfoMapper;
import com.wu.douban.mapper.BookInfoMapper;
import com.wu.douban.service.BookDetInfoService;
import com.wu.douban.service.BookInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author team04
 * @since 2021-11-29
 */
@Service
public class BookDetInfoServiceImpl extends ServiceImpl<BookDetInfoMapper, BookDetInfo> implements BookDetInfoService {

}
