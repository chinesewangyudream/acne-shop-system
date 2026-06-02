package com.acneshop.controller.h5;

import com.acneshop.common.Result;
import com.acneshop.entity.Appointment;
import com.acneshop.entity.ServiceItem;
import com.acneshop.security.SecurityUtils;
import com.acneshop.service.AppointmentService;
import com.acneshop.service.ServiceItemService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/h5/appointment")
@RequiredArgsConstructor
public class H5AppointmentController {

    private final AppointmentService appointmentService;
    private final ServiceItemService serviceItemService;

    @GetMapping("/services")
    public Result<List<ServiceItem>> services(@RequestParam Long storeId) {
        List<ServiceItem> items = serviceItemService.list(
                new LambdaQueryWrapper<ServiceItem>()
                        .eq(ServiceItem::getStoreId, storeId)
                        .eq(ServiceItem::getStatus, 1));
        return Result.success(items);
    }

    @PostMapping
    public Result<Void> create(@RequestBody Appointment appointment) {
        appointment.setCustomerId(SecurityUtils.getCurrentCustomerId());
        appointment.setSource(1); // 顾客自助
        appointment.setStatus(1); // 待确认
        appointmentService.save(appointment);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<List<Appointment>> myAppointments(@RequestParam Long storeId) {
        Long customerId = SecurityUtils.getCurrentCustomerId();
        List<Appointment> list = appointmentService.list(
                new LambdaQueryWrapper<Appointment>()
                        .eq(Appointment::getCustomerId, customerId)
                        .eq(Appointment::getStoreId, storeId)
                        .orderByDesc(Appointment::getAppointmentDate));
        return Result.success(list);
    }

    @PutMapping("/cancel/{id}")
    public Result<Void> cancel(@PathVariable Long id) {
        Appointment appointment = new Appointment();
        appointment.setId(id);
        appointment.setStatus(4); // 已取消
        appointmentService.updateById(appointment);
        return Result.success();
    }
}
