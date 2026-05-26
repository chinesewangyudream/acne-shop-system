package com.acneshop.service.impl;

import com.acneshop.entity.Product;
import com.acneshop.mapper.ProductMapper;
import com.acneshop.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {}
