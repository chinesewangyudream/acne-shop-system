package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Store;
import com.acneshop.service.StoreService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final StoreService storeService;

    @GetMapping("/page")
    public Result<Page<Store>> page(@RequestParam(defaultValue = "1") Integer current,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String name) {
        Page<Store> page = storeService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Store>()
                        .like(name != null, Store::getName, name)
                        .orderByDesc(Store::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Store store) {
        storeService.save(store);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Store store) {
        storeService.updateById(store);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        storeService.removeById(id);
        return Result.success();
    }
}
