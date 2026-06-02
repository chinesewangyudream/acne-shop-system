package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("customer")
public class Customer extends BaseEntity {
    private Long storeId;
    private String name;
    private Integer gender; // 0-女 1-男
    private Integer age;
    private String phone;
    private String passwordHash;
    private String allergyHistory;
    private String acneType;
    private Integer severity; // 1-轻度 2-中度 3-重度
    private String tags;
    private String sourceChannel;
    private Integer isBlacklisted; // 0-否 1-是
    private String blacklistReason;
    private LocalDateTime lastVisitAt;
}
