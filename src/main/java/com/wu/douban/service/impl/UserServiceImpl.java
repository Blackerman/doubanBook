package com.wu.douban.service.impl;

import com.wu.douban.entity.User;
import com.wu.douban.mapper.UserMapper;
import com.wu.douban.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
