package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("employee")
public class Employee extends BaseEntity {
    private Long storeId;
    private String name;
    private String phone;
    private String passwordHash;
    private Integer role; // 1-老板 2-店长 3-前台 4-美容师
    private String skills;
    private String commissionRules; // JSON
    private Integer status; // 1-在职 0-离职
}
