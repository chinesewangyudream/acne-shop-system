package com.acneshop.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未登录或token已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数错误"),
    USER_EXISTS(1001, "用户已存在"),
    USER_NOT_FOUND(1002, "用户不存在"),
    PASSWORD_ERROR(1003, "密码错误"),
    CARD_NOT_FOUND(2001, "卡项不存在"),
    CARD_EXPIRED(2002, "卡项已过期"),
    CARD_NO_REMAINING(2003, "次卡次数已用完"),
    CARD_STORE_LIMIT(2004, "卡项仅限办卡门店使用"),
    APPOINTMENT_CONFLICT(3001, "预约时段冲突"),
    STORE_NOT_FOUND(4001, "门店不存在"),
    SMS_CODE_ERROR(5001, "验证码错误"),
    SMS_CODE_EXPIRED(5002, "验证码已过期");

    private final int code;
    private final String message;
}
