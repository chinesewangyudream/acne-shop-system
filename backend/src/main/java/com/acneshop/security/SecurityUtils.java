package com.acneshop.security;

import com.acneshop.entity.Employee;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Employee getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Employee) auth.getPrincipal();
    }

    public static Long getCurrentStoreId() {
        return getCurrentUser().getStoreId();
    }

    public static Integer getCurrentRole() {
        return getCurrentUser().getRole();
    }

    public static boolean isBoss() {
        return Integer.valueOf(1).equals(getCurrentRole());
    }

    public static boolean isManager() {
        return Integer.valueOf(2).equals(getCurrentRole());
    }

    public static Long effectiveStoreId(Long requestStoreId) {
        if (isBoss()) {
            return requestStoreId;
        }
        return getCurrentStoreId();
    }
}
