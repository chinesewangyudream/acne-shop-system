package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("period_card")
public class PeriodCard extends BaseEntity {
    private Long customerId;
    private Long storeId;
    private Integer cardType; // 1-月卡 2-年卡
    private LocalDate startDate;
    private LocalDate endDate;
    private String benefits; // JSON
    private BigDecimal purchasePrice;
    private Integer status; // 1-正常 0-已退 2-已过期
}
