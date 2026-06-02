package com.acneshop.controller.h5;

import com.acneshop.common.Result;
import com.acneshop.common.ResultCode;
import com.acneshop.entity.Customer;
import com.acneshop.entity.Store;
import com.acneshop.mapper.CustomerMapper;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class H5StoreController {

    private final StoreService storeService;
    private final CustomerMapper customerMapper;

    /**
     * 获取营业中的门店列表（公开接口，放在 /h5/auth/ 下自动放行）
     */
    @GetMapping("/h5/auth/store/list")
    public Result<List<Store>> list() {
        List<Store> stores = storeService.list(
                new LambdaQueryWrapper<Store>()
                        .eq(Store::getStatus, 1)
                        .orderByAsc(Store::getId));
        return Result.success(stores);
    }

    /**
     * 顾客绑定门店（需登录）
     * 将顾客的 storeId 更新为所选门店，确保该门店能看到此顾客
     */
    @PutMapping("/h5/store/bind")
    public Result<Void> bind(@RequestParam Long storeId) {
        Long customerId = SecurityUtils.getCurrentCustomerId();
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            return Result.fail(ResultCode.USER_NOT_FOUND);
        }
        customer.setStoreId(storeId);
        customerMapper.updateById(customer);
        return Result.success();
    }
}
