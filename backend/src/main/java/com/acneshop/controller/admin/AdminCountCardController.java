package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.CountCard;
import com.acneshop.entity.Customer;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.CountCardService;
import com.acneshop.service.CustomerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/count-card")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminCountCardController {

    private final CountCardService countCardService;
    private final CustomerService customerService;

    @GetMapping("/page")
    public Result<Page<CountCard>> page(@RequestParam(defaultValue = "1") Integer current,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(required = false) Long storeId,
                                        @RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) Long customerId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<CountCard> page = countCardService.page(new Page<>(current, size),
                new LambdaQueryWrapper<CountCard>()
                        .eq(effectiveStoreId != null, CountCard::getStoreId, effectiveStoreId)
                        .eq(status != null, CountCard::getStatus, status)
                        .eq(customerId != null, CountCard::getCustomerId, customerId)
                        .orderByDesc(CountCard::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody CountCard card) {
        if (!SecurityUtils.isBoss()) {
            card.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (card.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        if (card.getRemainingCount() == null) {
            card.setRemainingCount(card.getTotalCount());
        }
        countCardService.save(card);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody CountCard card) {
        if (!SecurityUtils.isBoss()) {
            card.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        countCardService.updateById(card);
        return Result.success();
    }

    @PutMapping("/use/{id}")
    public Result<Void> use(@PathVariable Long id) {
        CountCard card = countCardService.getById(id);
        if (card == null) {
            return Result.fail(404, "次卡不存在");
        }
        if (card.getRemainingCount() <= 0) {
            return Result.fail(400, "次卡次数已用完");
        }
        card.setRemainingCount(card.getRemainingCount() - 1);
        if (card.getRemainingCount() == 0) {
            card.setStatus(2); // 已用完
        }
        countCardService.updateById(card);
        // 更新客户最后到店时间
        Customer customer = customerService.getById(card.getCustomerId());
        if (customer != null) {
            customer.setLastVisitAt(LocalDateTime.now());
            customerService.updateById(customer);
        }
        return Result.success();
    }

    @PutMapping("/refund/{id}")
    public Result<Void> refund(@PathVariable Long id) {
        CountCard card = countCardService.getById(id);
        if (card == null) {
            return Result.fail(404, "次卡不存在");
        }
        card.setStatus(0); // 已退
        countCardService.updateById(card);
        return Result.success();
    }
}
