package com.acneshop.service.impl;

import com.acneshop.entity.MemberCard;
import com.acneshop.mapper.MemberCardMapper;
import com.acneshop.service.MemberCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MemberCardServiceImpl extends ServiceImpl<MemberCardMapper, MemberCard> implements MemberCardService {}
