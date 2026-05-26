package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member_card")
public class MemberCard extends BaseEntity {

    private Long customerId;

    private Long storeId;

    private String cardType; // COUNT/YEAR/MONTH

    private String name;

    private BigDecimal price;

    private Integer totalCount; // 次卡总次数

    private Integer remainingCount; // 次卡剩余次数

    private LocalDate startDate;

    private LocalDate endDate;

    private String status; // ACTIVE/EXPIRED/REFUNDED

    private String benefits; // 权益内容JSON
}
