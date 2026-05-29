package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Product;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminProductController {

    private final ProductService productService;

    @GetMapping("/page")
    public Result<Page<Product>> page(@RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<Product> page = productService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Product>()
                        .like(name != null, Product::getName, name)
                        .eq(effectiveStoreId != null, Product::getStoreId, effectiveStoreId)
                        .orderByDesc(Product::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Product product) {
        if (!SecurityUtils.isBoss()) {
            product.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (product.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        productService.save(product);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Product product) {
        if (!SecurityUtils.isBoss()) {
            product.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        productService.updateById(product);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (!SecurityUtils.isBoss()) {
            Product existing = productService.getById(id);
            if (existing == null || !existing.getStoreId().equals(SecurityUtils.getCurrentStoreId())) {
                return Result.fail(403, "无权操作其他门店数据");
            }
        }
        productService.removeById(id);
        return Result.success();
    }

    @GetMapping("/warning")
    public Result<List<Product>> stockWarning(@RequestParam(required = false) Long storeId) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        List<Product> list = productService.list(
                new LambdaQueryWrapper<Product>()
                        .eq(effectiveStoreId != null, Product::getStoreId, effectiveStoreId)
                        .apply("stock <= warning_stock"));
        return Result.success(list);
    }
}
