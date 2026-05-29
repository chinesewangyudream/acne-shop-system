package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Customer;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.CustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/customer")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminCustomerController {

    private final CustomerService customerService;

    @GetMapping("/page")
    public Result<Page<Customer>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String phone,
                                       @RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<Customer> page = customerService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Customer>()
                        .like(name != null, Customer::getName, name)
                        .like(phone != null, Customer::getPhone, phone)
                        .eq(effectiveStoreId != null, Customer::getStoreId, effectiveStoreId)
                        .orderByDesc(Customer::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Customer customer) {
        if (!SecurityUtils.isBoss()) {
            customer.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (customer.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        customerService.save(customer);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Customer customer) {
        if (!SecurityUtils.isBoss()) {
            customer.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        customerService.updateById(customer);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!SecurityUtils.isBoss()) {
            Customer existing = customerService.getById(id);
            if (existing == null || !existing.getStoreId().equals(SecurityUtils.getCurrentStoreId())) {
                return Result.fail(403, "无权操作其他门店数据");
            }
        }
        customerService.removeById(id);
        return Result.success();
    }

    @PutMapping("/blacklist/{id}")
    public Result<Void> blacklist(@PathVariable Long id, @RequestParam String reason) {
        Customer customer = customerService.getById(id);
        if (customer == null) {
            return Result.fail(404, "客户不存在");
        }
        customer.setIsBlacklisted(1);
        customer.setBlacklistReason(reason);
        customerService.updateById(customer);
        return Result.success();
    }

    @PutMapping("/unblacklist/{id}")
    public Result<Void> unblacklist(@PathVariable Long id) {
        Customer customer = customerService.getById(id);
        if (customer == null) {
            return Result.fail(404, "客户不存在");
        }
        customer.setIsBlacklisted(0);
        customer.setBlacklistReason(null);
        customerService.updateById(customer);
        return Result.success();
    }
}
