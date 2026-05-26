package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Customer;
import com.acneshop.service.CustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/customer")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    @GetMapping("/page")
    public Result<Page<Customer>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) String phone,
                                       @RequestParam(required = false) Long storeId) {
        Page<Customer> page = customerService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Customer>()
                        .like(name != null, Customer::getName, name)
                        .like(phone != null, Customer::getPhone, phone)
                        .eq(storeId != null, Customer::getStoreId, storeId)
                        .orderByDesc(Customer::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Customer customer) {
        customerService.save(customer);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Customer customer) {
        customerService.updateById(customer);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.removeById(id);
        return Result.success();
    }
}
