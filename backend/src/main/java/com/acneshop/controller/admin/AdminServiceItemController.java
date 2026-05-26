package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.ServiceItem;
import com.acneshop.service.ServiceItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/service-item")
@RequiredArgsConstructor
public class AdminServiceItemController {

    private final ServiceItemService serviceItemService;

    @GetMapping("/page")
    public Result<Page<ServiceItem>> page(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long storeId) {
        Page<ServiceItem> page = serviceItemService.page(new Page<>(current, size),
                new LambdaQueryWrapper<ServiceItem>()
                        .eq(storeId != null, ServiceItem::getStoreId, storeId)
                        .orderByDesc(ServiceItem::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody ServiceItem item) {
        serviceItemService.save(item);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody ServiceItem item) {
        serviceItemService.updateById(item);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        serviceItemService.removeById(id);
        return Result.success();
    }
}
