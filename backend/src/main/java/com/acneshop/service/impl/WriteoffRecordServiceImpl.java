package com.acneshop.service.impl;

import com.acneshop.entity.WriteoffRecord;
import com.acneshop.mapper.WriteoffRecordMapper;
import com.acneshop.service.WriteoffRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WriteoffRecordServiceImpl extends ServiceImpl<WriteoffRecordMapper, WriteoffRecord> implements WriteoffRecordService {
}
