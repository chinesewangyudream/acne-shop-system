package com.acneshop.controller.h5;

import com.acneshop.common.Result;
import com.acneshop.entity.CountCard;
import com.acneshop.entity.PeriodCard;
import com.acneshop.service.CountCardService;
import com.acneshop.service.PeriodCardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/h5/card")
@RequiredArgsConstructor
public class H5CardController {

    private final CountCardService countCardService;
    private final PeriodCardService periodCardService;

    @GetMapping("/list")
    public Result<Map<String, List<?>>> list(@RequestParam Long customerId,
                                             @RequestParam Long storeId) {
        List<CountCard> countCards = countCardService.list(
                new LambdaQueryWrapper<CountCard>()
                        .eq(CountCard::getCustomerId, customerId)
                        .eq(CountCard::getStoreId, storeId)
                        .eq(CountCard::getStatus, 1)
                        .orderByDesc(CountCard::getCreateTime));

        List<PeriodCard> periodCards = periodCardService.list(
                new LambdaQueryWrapper<PeriodCard>()
                        .eq(PeriodCard::getCustomerId, customerId)
                        .eq(PeriodCard::getStoreId, storeId)
                        .eq(PeriodCard::getStatus, 1)
                        .orderByDesc(PeriodCard::getCreateTime));

        Map<String, List<?>> result = new HashMap<>();
        result.put("countCards", countCards);
        result.put("periodCards", periodCards);
        return Result.success(result);
    }

    @GetMapping("/count/{id}")
    public Result<CountCard> countDetail(@PathVariable Long id) {
        return Result.success(countCardService.getById(id));
    }

    @GetMapping("/period/{id}")
    public Result<PeriodCard> periodDetail(@PathVariable Long id) {
        return Result.success(periodCardService.getById(id));
    }
}
