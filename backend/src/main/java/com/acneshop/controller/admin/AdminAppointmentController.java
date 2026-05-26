package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.Appointment;
import com.acneshop.service.AppointmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/appointment")
@RequiredArgsConstructor
public class AdminAppointmentController {

    private final AppointmentService appointmentService;

    @GetMapping("/page")
    public Result<Page<Appointment>> page(@RequestParam(defaultValue = "1") Integer current,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(required = false) Long storeId,
                                          @RequestParam(required = false) String status,
                                          @RequestParam(required = false) LocalDate date) {
        Page<Appointment> page = appointmentService.page(new Page<>(current, size),
                new LambdaQueryWrapper<Appointment>()
                        .eq(storeId != null, Appointment::getStoreId, storeId)
                        .eq(status != null, Appointment::getStatus, status)
                        .eq(date != null, Appointment::getAppointmentDate, date)
                        .orderByDesc(Appointment::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody Appointment appointment) {
        appointment.setSource("STAFF");
        appointmentService.save(appointment);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus("CANCELLED");
        appointmentService.updateById(appointment);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    public Result<Void> complete(@PathVariable Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus("COMPLETED");
        appointmentService.updateById(appointment);
        return Result.success();
    }
}
