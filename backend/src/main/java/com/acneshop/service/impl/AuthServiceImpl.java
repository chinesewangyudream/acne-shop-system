package com.acneshop.service.impl;

import com.acneshop.common.ResultCode;
import com.acneshop.dto.LoginDTO;
import com.acneshop.entity.Employee;
import com.acneshop.exception.BusinessException;
import com.acneshop.mapper.EmployeeMapper;
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

    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";

    @Override
    public LoginVO login(LoginDTO dto) {
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getPhone, dto.getPhone()));
        if (employee == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(dto.getPassword(), employee.getPasswordHash())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        if (employee.getStatus() == 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "账号已停用");
        }

        String token = jwtTokenProvider.generateToken(employee.getId(), employee.getPhone(), employee.getRole(), employee.getStoreId());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setEmployeeId(employee.getId());
        vo.setName(employee.getName());
        vo.setPhone(employee.getPhone());
        vo.setRole(employee.getRole());
        vo.setStoreId(employee.getStoreId());
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

        String token = jwtTokenProvider.generateToken(0L, phone, 0, null);

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setPhone(phone);
        vo.setRole(0);
        return vo;
    }

    public void sendSmsCode(String phone) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        redisTemplate.opsForValue().set(SMS_CODE_PREFIX + phone, code, 5, TimeUnit.MINUTES);
    }
}
