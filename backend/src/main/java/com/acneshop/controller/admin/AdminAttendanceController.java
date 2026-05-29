package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Attendance;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.AttendanceService;
import com.acneshop.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/attendance")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER')")
public class AdminAttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    @GetMapping("/page")
    public Result<Page<Attendance>> page(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) Long employeeId,
                                         @RequestParam(required = false) LocalDate workDate) {
        Page<Attendance> page = attendanceService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Attendance>()
                        .eq(employeeId != null, Attendance::getEmployeeId, employeeId)
                        .eq(workDate != null, Attendance::getWorkDate, workDate)
                        .orderByDesc(Attendance::getWorkDate));
        return Result.success(page);
    }

    @PostMapping("/clock-in")
    public Result<Void> clockIn(@RequestParam Long employeeId) {
        Attendance record = new Attendance();
        record.setEmployeeId(employeeId);
        record.setWorkDate(LocalDate.now());
        record.setClockIn(LocalDateTime.now());
        attendanceService.save(record);
        return Result.success();
    }

    @PutMapping("/clock-out/{id}")
    public Result<Void> clockOut(@PathVariable Long id) {
        Attendance record = attendanceService.getById(id);
        if (record == null) {
            return Result.fail(404, "考勤记录不存在");
        }
        record.setClockOut(LocalDateTime.now());
        attendanceService.updateById(record);
        return Result.success();
    }

    @PutMapping("/leave-approve/{id}")
    public Result<Void> approveLeave(@PathVariable Long id) {
        Attendance record = attendanceService.getById(id);
        if (record == null) {
            return Result.fail(404, "考勤记录不存在");
        }
        record.setLeaveStatus(2); // 已批准
        attendanceService.updateById(record);
        return Result.success();
    }

    @PutMapping("/leave-reject/{id}")
    public Result<Void> rejectLeave(@PathVariable Long id) {
        Attendance record = attendanceService.getById(id);
        if (record == null) {
            return Result.fail(404, "考勤记录不存在");
        }
        record.setLeaveStatus(3); // 已拒绝
        attendanceService.updateById(record);
        return Result.success();
    }
}
