package com.acneshop.controller.admin;

import com.acneshop.common.Result;
import com.acneshop.entity.MemberCard;
import com.acneshop.service.MemberCardService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/member-card")
@RequiredArgsConstructor
public class AdminMemberCardController {

    private final MemberCardService memberCardService;

    @GetMapping("/page")
    public Result<Page<MemberCard>> page(@RequestParam(defaultValue = "1") Integer current,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @RequestParam(required = false) Long customerId,
                                         @RequestParam(required = false) String cardType,
                                         @RequestParam(required = false) String status) {
        Page<MemberCard> page = memberCardService.page(new Page<>(current, size),
                new LambdaQueryWrapper<MemberCard>()
                        .eq(customerId != null, MemberCard::getCustomerId, customerId)
                        .eq(cardType != null, MemberCard::getCardType, cardType)
                        .eq(status != null, MemberCard::getStatus, status)
                        .orderByDesc(MemberCard::getCreateTime));
        return Result.success(page);
    }

    @PostMapping
    public Result<Void> add(@RequestBody MemberCard memberCard) {
        memberCard.setStatus("ACTIVE");
        if ("COUNT".equals(memberCard.getCardType())) {
            memberCard.setRemainingCount(memberCard.getTotalCount());
        }
        memberCardService.save(memberCard);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody MemberCard memberCard) {
        memberCardService.updateById(memberCard);
        return Result.success();
    }

    @PutMapping("/use/{id}")
    public Result<Void> useCard(@PathVariable Long id) {
        MemberCard card = memberCardService.getById(id);
        if (card == null) {
            return Result.fail(2001, "卡项不存在");
        }
        if ("COUNT".equals(card.getCardType())) {
            if (card.getRemainingCount() <= 0) {
                return Result.fail(2003, "次卡次数已用完");
            }
            card.setRemainingCount(card.getRemainingCount() - 1);
        }
        memberCardService.updateById(card);
        return Result.success();
    }
}
