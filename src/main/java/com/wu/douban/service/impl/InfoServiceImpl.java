package com.wu.douban.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wu.douban.entity.Info;
import com.wu.douban.entity.User;
import com.wu.douban.mapper.InfoMapper;
import com.wu.douban.mapper.UserMapper;
import com.wu.douban.service.InfoService;
import com.wu.douban.service.UserService;
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
public class InfoServiceImpl extends ServiceImpl<InfoMapper, Info> implements InfoService {

}
