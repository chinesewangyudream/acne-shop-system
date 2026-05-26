package com.acneshop.service.impl;

import com.acneshop.entity.Store;
import com.acneshop.mapper.StoreMapper;
import com.acneshop.service.StoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {}
