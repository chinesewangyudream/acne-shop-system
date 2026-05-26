package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("appointment")
public class Appointment extends BaseEntity {

    private Long customerId;

    private Long storeId;

    private Long serviceItemId;

    private Long beauticianId;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private String status; // PENDING/CONFIRMED/CANCELLED/COMPLETED

    private String remark;

    private String source; // SELF/STAFF
}
