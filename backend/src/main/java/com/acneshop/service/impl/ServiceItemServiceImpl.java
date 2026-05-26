package com.acneshop.service.impl;

import com.acneshop.entity.ServiceItem;
import com.acneshop.mapper.ServiceItemMapper;
import com.acneshop.service.ServiceItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ServiceItemServiceImpl extends ServiceImpl<ServiceItemMapper, ServiceItem> implements ServiceItemService {}
