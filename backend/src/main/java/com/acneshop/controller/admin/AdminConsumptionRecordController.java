package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.ConsumptionRecord;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.ConsumptionRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/consumption-record")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminConsumptionRecordController {

    private final ConsumptionRecordService consumptionRecordService;

    @GetMapping("/page")
    public Result<Page<ConsumptionRecord>> page(@RequestParam(defaultValue = "1") Integer current,
                                                @RequestParam(defaultValue = "10") Integer size,
                                                @RequestParam(required = false) Long storeId,
                                                @RequestParam(required = false) Long customerId,
                                                @RequestParam(required = false) Integer type) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<ConsumptionRecord> page = consumptionRecordService.page(new Page<>(current, size),
                new LambdaQueryWrapper<ConsumptionRecord>()
                        .eq(effectiveStoreId != null, ConsumptionRecord::getStoreId, effectiveStoreId)
                        .eq(customerId != null, ConsumptionRecord::getCustomerId, customerId)
                        .eq(type != null, ConsumptionRecord::getType, type)
                        .orderByDesc(ConsumptionRecord::getCreatedAt));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody ConsumptionRecord record) {
        if (!SecurityUtils.isBoss()) {
            record.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (record.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        record.setOperatorId(SecurityUtils.getCurrentUser().getId());
        consumptionRecordService.save(record);
        return Result.success();
    }
}
