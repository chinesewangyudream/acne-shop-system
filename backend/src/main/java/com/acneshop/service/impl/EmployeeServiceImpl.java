package com.acneshop.service.impl;

import com.acneshop.entity.Employee;
import com.acneshop.mapper.EmployeeMapper;
import com.acneshop.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
