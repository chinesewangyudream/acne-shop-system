package com.acneshop.service.impl;

import com.acneshop.entity.Customer;
import com.acneshop.mapper.CustomerMapper;
import com.acneshop.service.CustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {}
