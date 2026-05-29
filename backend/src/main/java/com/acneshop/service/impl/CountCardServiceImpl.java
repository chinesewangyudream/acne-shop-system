package com.acneshop.service.impl;

import com.acneshop.entity.CountCard;
import com.acneshop.mapper.CountCardMapper;
import com.acneshop.service.CountCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CountCardServiceImpl extends ServiceImpl<CountCardMapper, CountCard> implements CountCardService {
}
