package com.acneshop.controller.h5;

import com.acneshop.common.Result;
import com.acneshop.entity.MemberCard;
import com.acneshop.service.MemberCardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/h5/card")
@RequiredArgsConstructor
public class H5CardController {

    private final MemberCardService memberCardService;

    @GetMapping("/list")
    public Result<List<MemberCard>> list(@RequestParam String phone,
                                         @RequestParam Long storeId) {
        // 根据手机号关联客户查询该门店的卡项
        List<MemberCard> cards = memberCardService.list(
                new LambdaQueryWrapper<MemberCard>()
                        .eq(MemberCard::getStoreId, storeId)
                        .eq(MemberCard::getStatus, "ACTIVE")
                        .orderByDesc(MemberCard::getCreateTime));
        return Result.success(cards);
    }

    @GetMapping("/detail/{id}")
    public Result<MemberCard> detail(@PathVariable Long id) {
        return Result.success(memberCardService.getById(id));
    }
}
