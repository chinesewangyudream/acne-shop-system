package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {

    private String username;

    private String password;

    private String realName;

    private String phone;

    private String role; // ADMIN/STORE_MANAGER/RECEPTIONIST/BEAUTICIAN

    private Long storeId;

    private String skillTags;

    private Integer status;
}
