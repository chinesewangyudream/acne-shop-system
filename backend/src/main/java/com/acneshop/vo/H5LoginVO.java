package com.acneshop.vo;

import lombok.Data;

@Data
public class H5LoginVO {
    private String token;
    private Long customerId;
    private String name;
    private String phone;
    private Long storeId;
}
