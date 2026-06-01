package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.ConsumptionRecord;
import com.acneshop.entity.Customer;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.ConsumptionRecordService;
import com.acneshop.service.CustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/consumption-record")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminConsumptionRecordController {

    private final ConsumptionRecordService consumptionRecordService;
    private final CustomerService customerService;

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
        // 消费记录新增时更新客户最后到店时间（退款类型除外）
        if (record.getType() != null && record.getType() != 5 && record.getCustomerId() != null) {
            Customer customer = customerService.getById(record.getCustomerId());
            if (customer != null) {
                customer.setLastVisitAt(LocalDateTime.now());
                customerService.updateById(customer);
            }
        }
        return Result.success();
    }
}
