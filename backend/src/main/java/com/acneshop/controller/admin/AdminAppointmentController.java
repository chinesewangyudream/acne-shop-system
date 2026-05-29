package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Appointment;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.AppointmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/appointment")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'MANAGER', 'RECEPTIONIST')")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/page")
    public Result<Page<Appointment>> page(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long storeId,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) LocalDate date) {
        Long effectiveStoreId = SecurityUtils.effectiveStoreId(storeId);
        Page<Appointment> page = appointmentService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Appointment>()
                        .eq(effectiveStoreId != null, Appointment::getStoreId, effectiveStoreId)
                        .eq(status != null, Appointment::getStatus, status)
                        .eq(date != null, Appointment::getAppointmentDate, date)
                        .orderByDesc(Appointment::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Appointment appointment) {
        if (!SecurityUtils.isBoss()) {
            appointment.setStoreId(SecurityUtils.getCurrentStoreId());
        }
        if (appointment.getStoreId() == null) {
            return Result.fail(400, "请选择所属门店");
        }
        appointment.setSource(2); // 前台代约
        if (appointment.getStatus() == null) {
            appointment.setStatus(1); // 待确认
        }
        appointmentService.save(appointment);
        return Result.success();
    }

    @PutMapping("/confirm/{id}")
    public Result<Void> confirm(@PathVariable Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus(2); // 已确认
        appointmentService.updateById(appointment);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus(3); // 已完成
        appointmentService.updateById(appointment);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus(4); // 已取消
        appointmentService.updateById(appointment);
        return Result.success();
    }

    @PutMapping("/reschedule/{id}")
    public Result<Void> reschedule(@PathVariable Long id, @RequestBody Appointment appointment) {
        appointment.setId(id);
        appointment.setStatus(5); // 已改期
        appointmentService.updateById(appointment);
        return Result.success();
    }
}
