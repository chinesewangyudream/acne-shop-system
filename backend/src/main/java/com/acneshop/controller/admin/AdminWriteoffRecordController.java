package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.WriteoffRecord;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.WriteoffRecordService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/writeoff-record")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminWriteoffRecordController {

    private final WriteoffRecordService writeoffRecordService;

    @GetMapping("/page")
    public Result<Page<WriteoffRecord>> page(@RequestParam(defaultValue = "1") Integer current,
                                              @RequestParam(defaultValue = "10") Integer size,
                                              @RequestParam(required = false) Long storeId,
                                              @RequestParam(required = false) Long customerId,
                                              @RequestParam(required = false) Integer cardType,
                                              @RequestParam(required = false) Long cardId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<WriteoffRecord> page = writeoffRecordService.page(new Page<>(current, size),
                new LambdaQueryWrapper<WriteoffRecord>()
                        .eq(effectiveStoreId != null, WriteoffRecord::getStoreId, effectiveStoreId)
                        .eq(customerId != null, WriteoffRecord::getCustomerId, customerId)
                        .eq(cardType != null, WriteoffRecord::getCardType, cardType)
                        .eq(cardId != null, WriteoffRecord::getCardId, cardId)
                        .orderByDesc(WriteoffRecord::getCreatedAt));
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<WriteoffRecord> detail(@PathVariable Long id) {
        WriteoffRecord record = writeoffRecordService.getById(id);
        if (record == null) {
            return Result.fail(404, "核销记录不存在");
        }
        return Result.success(record);
    }
}
