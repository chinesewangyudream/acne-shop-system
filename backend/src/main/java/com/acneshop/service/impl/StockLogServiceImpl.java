package com.acneshop.service.impl;

import com.acneshop.entity.StockLog;
import com.acneshop.mapper.StockLogMapper;
import com.acneshop.service.StockLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StockLogServiceImpl extends ServiceImpl<StockLogMapper, StockLog> implements StockLogService {
}
