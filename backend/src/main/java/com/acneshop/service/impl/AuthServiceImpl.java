package com.acneshop.service.impl;

import com.acneshop.common.ResultCode;
import com.acneshop.dto.H5RegisterDTO;
import com.acneshop.dto.LoginDTO;
import com.acneshop.entity.Customer;
import com.acneshop.entity.Employee;
import com.acneshop.exception.BusinessException;
import com.acneshop.mapper.CustomerMapper;
import com.acneshop.mapper.EmployeeMapper;
import com.acneshop.security.JwtTokenProvider;
import com.acneshop.security.UserPrincipal;
import com.acneshop.service.AuthService;
import com.acneshop.vo.H5LoginVO;
import com.acneshop.vo.LoginVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmployeeMapper employeeMapper;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

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

        String token = jwtTokenProvider.generateToken(employee.getId(), employee.getPhone(),
                employee.getRole(), employee.getStoreId(), UserPrincipal.TYPE_EMPLOYEE);

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
    public H5LoginVO h5Login(String phone, String password) {
        Customer customer = customerMapper.selectOne(
                new LambdaQueryWrapper<Customer>().eq(Customer::getPhone, phone));
        if (customer == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        if (customer.getPasswordHash() == null || customer.getPasswordHash().isEmpty()) {
            throw new BusinessException(ResultCode.CUSTOMER_NOT_REGISTERED);
        }
        if (!passwordEncoder.matches(password, customer.getPasswordHash())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        if (Integer.valueOf(1).equals(customer.getIsBlacklisted())) {
            throw new BusinessException(ResultCode.CUSTOMER_BLACKLISTED);
        }

        String token = jwtTokenProvider.generateToken(customer.getId(), customer.getPhone(),
                0, customer.getStoreId(), UserPrincipal.TYPE_CUSTOMER);

        H5LoginVO vo = new H5LoginVO();
        vo.setToken(token);
        vo.setCustomerId(customer.getId());
        vo.setName(customer.getName());
        vo.setPhone(customer.getPhone());
        vo.setStoreId(customer.getStoreId());
        return vo;
    }

    @Override
    public void h5Register(H5RegisterDTO dto) {
        Customer existing = customerMapper.selectOne(
                new LambdaQueryWrapper<Customer>().eq(Customer::getPhone, dto.getPhone()));

        if (existing != null) {
            // 已有记录且已设置密码 → 已注册
            if (existing.getPasswordHash() != null && !existing.getPasswordHash().isEmpty()) {
                throw new BusinessException(ResultCode.USER_EXISTS);
            }
            // 已有记录但未设置密码（管理员录入的）→ 关联账号，只更新密码
            existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            if (dto.getName() != null && !dto.getName().isEmpty()) {
                existing.setName(dto.getName());
            }
            if (dto.getStoreId() != null) {
                existing.setStoreId(dto.getStoreId());
            }
            customerMapper.updateById(existing);
            return;
        }

        // 新用户注册
        Customer customer = new Customer();
        customer.setPhone(dto.getPhone());
        customer.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        customer.setName(dto.getName() != null && !dto.getName().isEmpty()
                ? dto.getName() : "用户" + dto.getPhone().substring(7));
        customer.setStoreId(dto.getStoreId());
        customer.setIsBlacklisted(0);
        customerMapper.insert(customer);
    }
}
