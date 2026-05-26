package com.acneshop.service.impl;

import com.acneshop.common.ResultCode;
import com.acneshop.dto.LoginDTO;
import com.acneshop.entity.User;
import com.acneshop.exception.BusinessException;
import com.acneshop.mapper.UserMapper;
import com.acneshop.security.JwtTokenProvider;
import com.acneshop.service.AuthService;
import com.acneshop.vo.LoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    @Override
    public LoginVO login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "账号已停用");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername(), user.getRole(), user.getStoreId());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRole(user.getRole());
        vo.setStoreId(user.getStoreId());
        return vo;
    }

    @Override
    public LoginVO h5Login(String phone, String code) {
        String redisKey = SMS_CODE_PREFIX + phone;
        Object savedCode = redisTemplate.opsForValue().get(redisKey);
        if (savedCode == null) {
            throw new BusinessException(ResultCode.SMS_CODE_EXPIRED);
        }
        if (!code.equals(savedCode.toString())) {
            throw new BusinessException(ResultCode.SMS_CODE_ERROR);
        }

        // H5登录用手机号匹配客户，生成临时token
        String token = jwtTokenProvider.generateToken(0L, phone, "CUSTOMER", null);

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUsername(phone);
        vo.setRole("CUSTOMER");
        return vo;
    }

    public void sendSmsCode(String phone) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        redisTemplate.opsForValue().set(SMS_CODE_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        // TODO: 对接短信服务商发送验证码
    }
}
