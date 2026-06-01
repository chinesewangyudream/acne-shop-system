package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("skin_record")
public class SkinRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long customerId;
    private String acneType;
    private Integer severity;
    private String affectedAreas;
    private String photoUrls; // JSON
    private String note;
    private LocalDate recordDate;
    private LocalDateTime createdAt;
    @TableLogic
    private Integer deleted;
}
