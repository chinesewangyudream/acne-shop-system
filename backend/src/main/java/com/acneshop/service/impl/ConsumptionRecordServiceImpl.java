package com.acneshop.service.impl;

import com.acneshop.entity.ConsumptionRecord;
import com.acneshop.mapper.ConsumptionRecordMapper;
import com.acneshop.service.ConsumptionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ConsumptionRecordServiceImpl extends ServiceImpl<ConsumptionRecordMapper, ConsumptionRecord> implements ConsumptionRecordService {
}
