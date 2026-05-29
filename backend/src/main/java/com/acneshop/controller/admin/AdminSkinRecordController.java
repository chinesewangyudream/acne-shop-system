package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.SkinRecord;
import com.acneshop.service.SkinRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/skin-record")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminSkinRecordController {

    private final SkinRecordService skinRecordService;

    @GetMapping("/page")
    public Result<Page<SkinRecord>> page(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam Long customerId) {
        Page<SkinRecord> page = skinRecordService.page(new Page<>(current, size),
                new LambdaQueryWrapper<SkinRecord>()
                        .eq(SkinRecord::getCustomerId, customerId)
                        .orderByDesc(SkinRecord::getRecordDate));
        return Result.success(page);
    }

    @GetMapping("/list")
    public Result<List<SkinRecord>> list(@RequestParam Long customerId) {
        return Result.success(skinRecordService.list(
                new LambdaQueryWrapper<SkinRecord>()
                        .eq(SkinRecord::getCustomerId, customerId)
                        .orderByDesc(SkinRecord::getRecordDate)));
    }

    @PostMapping
    public Result<Void> add(@RequestBody SkinRecord record) {
        skinRecordService.save(record);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody SkinRecord record) {
        skinRecordService.updateById(record);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        skinRecordService.removeById(id);
        return Result.success();
    }
}
