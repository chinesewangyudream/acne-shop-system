package com.acneshop.security;

import lombok.Data;

/**
 * 统一认证主体，同时支持员工和顾客
 */
@Data
public class UserPrincipal {

    public static final String TYPE_EMPLOYEE = "EMPLOYEE";
    public static final String TYPE_CUSTOMER = "CUSTOMER";

    private Long id;
    private String name;
    private String phone;
    private Integer role;
    private Long storeId;
    private String userType;

    public boolean isCustomer() {
        return TYPE_CUSTOMER.equals(userType);
    }

    public boolean isEmployee() {
        return TYPE_EMPLOYEE.equals(userType);
    }
}
