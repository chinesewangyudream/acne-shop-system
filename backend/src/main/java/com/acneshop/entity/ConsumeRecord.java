package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("consume_record")
public class ConsumeRecord extends BaseEntity {

    private Long customerId;

    private Long storeId;

    private Long memberCardId;

    private String type; // SERVICE/PRODUCT/RECHARGE/REFUND

    private BigDecimal amount;

    private String payMethod; // CASH/WECHAT/ALIPAY/CARD

    private String description;
}
