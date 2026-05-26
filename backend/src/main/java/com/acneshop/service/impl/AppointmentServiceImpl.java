package com.acneshop.service.impl;

import com.acneshop.entity.Appointment;
import com.acneshop.mapper.AppointmentMapper;
import com.acneshop.service.AppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentService {}
