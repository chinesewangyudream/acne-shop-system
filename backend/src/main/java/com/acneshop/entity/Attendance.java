package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("attendance")
public class Attendance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long employeeId;
    private LocalDate workDate;
    private LocalDateTime clockIn;
    private LocalDateTime clockOut;
    private Integer leaveType; // 1-事假 2-病假 3-年假
    private Integer leaveStatus; // 0-未请假 1-待审批 2-已批准 3-已拒绝
    private LocalDateTime createdAt;
}
