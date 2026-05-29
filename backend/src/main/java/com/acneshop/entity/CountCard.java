package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("count_card")
public class CountCard extends BaseEntity {
    private Long customerId;
    private Long storeId;
    private Long treatmentPlanId;
    private Integer totalCount;
    private Integer remainingCount;
    private BigDecimal purchasePrice;
    private Integer status; // 1-正常 0-已退 2-已用完
}
