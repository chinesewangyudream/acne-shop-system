package com.acneshop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("store")
public class Store extends BaseEntity {
    private String storeName;
    private String address;
    private String phone;
    private String businessHours;
    private String qrCodeUrl;
    private Integer status;
}
