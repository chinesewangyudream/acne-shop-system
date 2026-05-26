package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("product")
public class Product extends BaseEntity {

    private String name;

    private String spec;

    private BigDecimal purchasePrice;

    private BigDecimal sellPrice;

    private Integer stock;

    private Integer warningStock;

    private Long storeId;

    private Integer status;
}
