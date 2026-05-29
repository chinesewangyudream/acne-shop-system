package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("treatment_record")
public class TreatmentRecord extends BaseEntity {
    private Long customerId;
    private Long appointmentId;
    private Long employeeId;
    private Long serviceItemId;
    private String content;
    private String productsUsed; // JSON
    private String beforePhotos; // JSON
    private String afterPhotos; // JSON
    private String feedback;
    private LocalDateTime treatmentDate;
}
