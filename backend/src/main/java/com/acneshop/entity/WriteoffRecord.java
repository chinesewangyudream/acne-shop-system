package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("writeoff_record")
public class WriteoffRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private Long storeId;
    private Long cardId;
    private Integer cardType; // 1-次卡 2-年月卡
    private Long serviceItemId;
    private Long operatorId;
    private Integer remainingCount; // 核销后剩余次数（次卡专用）
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
