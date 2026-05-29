package com.acneshop.service.impl;

import com.acneshop.entity.Attendance;
import com.acneshop.mapper.AttendanceMapper;
import com.acneshop.service.AttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements AttendanceService {
}
