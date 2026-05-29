package com.acneshop.service.impl;

import com.acneshop.entity.OperationLog;
import com.acneshop.mapper.OperationLogMapper;
import com.acneshop.service.OperationLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
}
