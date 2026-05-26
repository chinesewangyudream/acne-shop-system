package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("treatment_record")
public class TreatmentRecord extends BaseEntity {

    private Long customerId;

    private Long storeId;

    private Long serviceItemId;

    private Long beauticianId;

    private Long memberCardId;

    private String content;

    private String productsUsed;

    private String beforePhoto;

    private String afterPhoto;

    private String feedback;
}
