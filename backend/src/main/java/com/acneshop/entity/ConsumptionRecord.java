package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("consumption_record")
public class ConsumptionRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long storeId;
    private Integer type; // 1-次卡消费 2-年月卡消费 3-充值 4-产品购买 5-退款
    private BigDecimal amount;
    private Long cardId;
    private Integer cardType; // 1-次卡 2-年月卡
    private String description;
    private Long operatorId;
    private LocalDateTime createdAt;
}
