package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.TreatmentPlan;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.TreatmentPlanService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/treatment-plan")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminTreatmentPlanController {

    private final TreatmentPlanService treatmentPlanService;

    @GetMapping("/page")
    public Result<Page<TreatmentPlan>> page(@RequestParam(defaultValue = "1") Integer current,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<TreatmentPlan> page = treatmentPlanService.page(new Page<>(current, size),
                new LambdaQueryWrapper<TreatmentPlan>()
                        .eq(effectiveStoreId != null, TreatmentPlan::getStoreId, effectiveStoreId)
                        .orderByDesc(TreatmentPlan::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody TreatmentPlan plan) {
        if (!SecurityUtils.isBoss()) {
            plan.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (plan.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        treatmentPlanService.save(plan);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody TreatmentPlan plan) {
        if (!SecurityUtils.isBoss()) {
            plan.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        treatmentPlanService.updateById(plan);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!SecurityUtils.isBoss()) {
            TreatmentPlan existing = treatmentPlanService.getById(id);
            if (existing == null || !existing.getStoreId().equals(SecurityUtils.getCurrentStoreId())) {
                return Result.fail(403, "无权操作其他门店数据");
            }
        }
        treatmentPlanService.removeById(id);
        return Result.success();
    }
}
