package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Product;
import com.acneshop.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping("/page")
    public Result<Page<Product>> page(@RequestParam(defaultValue = "1") Integer current,
                                      @RequestParam(defaultValue = "10") Integer size,
                                      @RequestParam(required = false) String name,
                                      @RequestParam(required = false) Long storeId) {
        Page<Product> page = productService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Product>()
                        .like(name != null, Product::getName, name)
                        .eq(storeId != null, Product::getStoreId, storeId)
                        .orderByDesc(Product::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Product product) {
        productService.save(product);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Product product) {
        productService.updateById(product);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        productService.removeById(id);
        return Result.success();
    }
}
