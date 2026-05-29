package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.PeriodCard;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.PeriodCardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/period-card")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminPeriodCardController {

    private final PeriodCardService periodCardService;

    @GetMapping("/page")
    public Result<Page<PeriodCard>> page(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) Long storeId,
                                         @RequestParam(required = false) Integer status,
                                         @RequestParam(required = false) Integer cardType,
                                         @RequestParam(required = false) Long customerId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<PeriodCard> page = periodCardService.page(new Page<>(current, size),
                new LambdaQueryWrapper<PeriodCard>()
                        .eq(effectiveStoreId != null, PeriodCard::getStoreId, effectiveStoreId)
                        .eq(status != null, PeriodCard::getStatus, status)
                        .eq(cardType != null, PeriodCard::getCardType, cardType)
                        .eq(customerId != null, PeriodCard::getCustomerId, customerId)
                        .orderByDesc(PeriodCard::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody PeriodCard card) {
        if (!SecurityUtils.isBoss()) {
            card.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (card.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        periodCardService.save(card);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody PeriodCard card) {
        if (!SecurityUtils.isBoss()) {
            card.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        periodCardService.updateById(card);
        return Result.success();
    }

    @PutMapping("/refund/{id}")
    public Result<Void> refund(@PathVariable Long id) {
        PeriodCard card = periodCardService.getById(id);
        if (card == null) {
            return Result.fail(404, "卡不存在");
        }
        card.setStatus(0); // 已退
        periodCardService.updateById(card);
        return Result.success();
    }
}
