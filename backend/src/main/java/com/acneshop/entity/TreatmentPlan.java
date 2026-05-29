package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("treatment_plan")
public class TreatmentPlan extends BaseEntity {
    private Long storeId;
    private String name;
    private Integer totalCount;
    private BigDecimal price;
    private String serviceItemIds; // JSON
    private String description;
    private Integer status; // 1-启用 0-停用
}
