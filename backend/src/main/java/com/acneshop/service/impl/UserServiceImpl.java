package com.acneshop.service.impl;

import com.acneshop.entity.User;
import com.acneshop.mapper.UserMapper;
import com.acneshop.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {}
