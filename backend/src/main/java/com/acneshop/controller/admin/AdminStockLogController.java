package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Product;
import com.acneshop.entity.StockLog;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.ProductService;
import com.acneshop.service.StockLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/stock-log")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminStockLogController {

    private final StockLogService stockLogService;
    private final ProductService productService;

    @GetMapping("/page")
    public Result<Page<StockLog>> page(@RequestParam(defaultValue = "1") Integer current,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       @RequestParam(required = false) Long productId) {
        Page<StockLog> page = stockLogService.page(new Page<>(current, size),
                new LambdaQueryWrapper<StockLog>()
                        .eq(productId != null, StockLog::getProductId, productId)
                        .orderByDesc(StockLog::getCreatedAt));
        return Result.success(page);
    }

    @PostMapping("/in")
    public Result<Void> stockIn(@RequestBody StockLog log) {
        log.setType(1); // 入库
        log.setOperatorId(SecurityUtils.getCurrentUser().getId());
        stockLogService.save(log);
        // 更新库存
        Product product = productService.getById(log.getProductId());
        if (product != null) {
            product.setStock(product.getStock() + log.getQuantity());
            productService.updateById(product);
        }
        return Result.success();
    }

    @PostMapping("/out")
    public Result<Void> stockOut(@RequestBody StockLog log) {
        Product product = productService.getById(log.getProductId());
        if (product == null) {
            return Result.fail(404, "产品不存在");
        }
        if (product.getStock() < log.getQuantity()) {
            return Result.fail(400, "库存不足");
        }
        log.setType(2); // 出库
        log.setOperatorId(SecurityUtils.getCurrentUser().getId());
        stockLogService.save(log);
        product.setStock(product.getStock() - log.getQuantity());
        productService.updateById(product);
        return Result.success();
    }
}
