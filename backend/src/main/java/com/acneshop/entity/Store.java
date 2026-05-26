package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store")
public class Store extends BaseEntity {

    private String name;

    private String address;

    private String phone;

    private String qrcode;

    private Integer status;
}
