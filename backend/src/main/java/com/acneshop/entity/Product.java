package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity {
    private Long storeId;
    private String name;
    private String specification;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Integer stock;
    private Integer warningStock;
    private String imageUrl;
    private Integer status;
}
