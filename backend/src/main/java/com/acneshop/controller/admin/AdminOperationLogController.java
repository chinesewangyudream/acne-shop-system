package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.OperationLog;
import com.acneshop.service.OperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/operation-log")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class AdminOperationLogController {

    private final OperationLogService operationLogService;

    @GetMapping("/page")
    public Result<Page<OperationLog>> page(@RequestParam(defaultValue = "1") Integer current,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           @RequestParam(required = false) String module,
                                           @RequestParam(required = false) Long operatorId) {
        Page<OperationLog> page = operationLogService.page(new Page<>(current, size),
                new LambdaQueryWrapper<OperationLog>()
                        .eq(module != null, OperationLog::getModule, module)
                        .eq(operatorId != null, OperationLog::getOperatorId, operatorId)
                        .orderByDesc(OperationLog::getCreatedAt));
        return Result.success(page);
    }
}
