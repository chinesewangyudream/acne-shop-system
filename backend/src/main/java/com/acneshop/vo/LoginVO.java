package com.acneshop.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private Long employeeId;
    private String name;
    private String phone;
    private Integer role;
    private Long storeId;
}
