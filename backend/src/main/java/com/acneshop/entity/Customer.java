package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {

    private String name;

    private String gender;

    private String phone;

    private LocalDate birthday;

    private String allergyHistory;

    private String acneType;

    private String skinType;

    private String severity;

    private String tags;

    private String source;

    private Long storeId;

    private Integer status;

    private LocalDateTime lastVisitTime;
}
