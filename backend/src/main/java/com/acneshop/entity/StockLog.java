package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("stock_log")
public class StockLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long productId;
    private Integer type; // 1-入库 2-出库
    private Integer quantity;
    private String reason;
    private Long operatorId;
    private LocalDateTime createdAt;
}
