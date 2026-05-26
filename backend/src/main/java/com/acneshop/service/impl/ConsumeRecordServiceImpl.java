package com.acneshop.service.impl;

import com.acneshop.entity.ConsumeRecord;
import com.acneshop.mapper.ConsumeRecordMapper;
import com.acneshop.service.ConsumeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ConsumeRecordServiceImpl extends ServiceImpl<ConsumeRecordMapper, ConsumeRecord> implements ConsumeRecordService {}
