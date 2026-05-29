package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.dto.LoginDTO;
import com.acneshop.entity.Employee;
import com.acneshop.mapper.EmployeeMapper;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.AuthService;
import com.acneshop.vo.LoginVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AuthService authService;
    private final EmployeeMapper employeeMapper;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }

    @GetMapping("/info")
    public Result<LoginVO> info() {
        Employee jwtUser = SecurityUtils.getCurrentUser();
        Employee employee = employeeMapper.selectById(jwtUser.getId());
        LoginVO vo = new LoginVO();
        vo.setEmployeeId(employee.getId());
        vo.setName(employee.getName());
        vo.setPhone(employee.getPhone());
        vo.setRole(employee.getRole());
        vo.setStoreId(employee.getStoreId());
        return Result.success(vo);
    }
}
