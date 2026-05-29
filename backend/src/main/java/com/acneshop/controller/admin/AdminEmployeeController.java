package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Employee;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/employee")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminEmployeeController {

    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/page")
    public Result<Page<Employee>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Long storeId,
                                       @RequestParam(required = false) Integer role) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<Employee> page = employeeService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Employee>()
                        .like(name != null, Employee::getName, name)
                        .eq(effectiveStoreId != null, Employee::getStoreId, effectiveStoreId)
                        .eq(role != null, Employee::getRole, role)
                        .orderByDesc(Employee::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Employee employee) {
        if (!SecurityUtils.isBoss()) {
            employee.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (employee.getStoreId() == null && employee.getRole() != 1) {
            return Result.fail(400, "请选择所属门店");
        }
        if (employee.getPasswordHash() != null) {
            employee.setPasswordHash(passwordEncoder.encode(employee.getPasswordHash()));
        }
        employeeService.save(employee);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Employee employee) {
        if (!SecurityUtils.isBoss()) {
            employee.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (employee.getPasswordHash() != null && !employee.getPasswordHash().startsWith("$2a$")) {
            employee.setPasswordHash(passwordEncoder.encode(employee.getPasswordHash()));
        }
        employeeService.updateById(employee);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!SecurityUtils.isBoss()) {
            Employee existing = employeeService.getById(id);
            if (existing == null || !existing.getStoreId().equals(SecurityUtils.getCurrentStoreId())) {
                return Result.fail(403, "无权操作其他门店数据");
            }
        }
        employeeService.removeById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        return Result.success(employeeService.list(
                new LambdaQueryWrapper<Employee>()
                        .eq(effectiveStoreId != null, Employee::getStoreId, effectiveStoreId)
                        .eq(Employee::getStatus, 1)));
    }
}
