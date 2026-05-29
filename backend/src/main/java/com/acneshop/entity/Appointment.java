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
    private Long employeeId;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer source; // 1-顾客自助 2-前台代约
    private Integer status; // 1-待确认 2-已确认 3-已完成 4-已取消 5-已改期
    private String remark;
}
