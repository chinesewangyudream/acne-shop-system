package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.ServiceItem;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.ServiceItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/service-item")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminServiceItemController {

    private final ServiceItemService serviceItemService;

    @GetMapping("/page")
    public Result<Page<ServiceItem>> page(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<ServiceItem> page = serviceItemService.page(new Page<>(current, size),
                new LambdaQueryWrapper<ServiceItem>()
                        .eq(effectiveStoreId != null, ServiceItem::getStoreId, effectiveStoreId)
                        .orderByDesc(ServiceItem::getCreateTime));
        return Result.success(page);
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        return Result.success(serviceItemService.list(
                new LambdaQueryWrapper<ServiceItem>()
                        .eq(effectiveStoreId != null, ServiceItem::getStoreId, effectiveStoreId)
                        .eq(ServiceItem::getStatus, 1)));
    }

    @PostMapping
    public Result<Void> add(@RequestBody ServiceItem item) {
        if (!SecurityUtils.isBoss()) {
            item.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (item.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        serviceItemService.save(item);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody ServiceItem item) {
        if (!SecurityUtils.isBoss()) {
            item.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        serviceItemService.updateById(item);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!SecurityUtils.isBoss()) {
            ServiceItem existing = serviceItemService.getById(id);
            if (existing == null || !existing.getStoreId().equals(SecurityUtils.getCurrentStoreId())) {
                return Result.fail(403, "无权操作其他门店数据");
            }
        }
        serviceItemService.removeById(id);
        return Result.success();
    }
}
