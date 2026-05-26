package com.acneshop.service.impl;

import com.acneshop.entity.TreatmentRecord;
import com.acneshop.mapper.TreatmentRecordMapper;
import com.acneshop.service.TreatmentRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class TreatmentRecordServiceImpl extends ServiceImpl<TreatmentRecordMapper, TreatmentRecord> implements TreatmentRecordService {}
