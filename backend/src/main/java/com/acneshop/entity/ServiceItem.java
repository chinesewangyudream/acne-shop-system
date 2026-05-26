package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("service_item")
public class ServiceItem extends BaseEntity {

    private String name;

    private BigDecimal price;

    private Integer duration; // 分钟

    private String description;

    private Long storeId;

    private Integer status;
}
