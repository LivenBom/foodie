package com.imooc.common.enums;

public enum ErrorCodeEnum {
    SYSTEM_ERROR(10000, "系统繁忙，请稍后再试"),
    CATEGORY_NAME_ALREADY_EXISTS(20001, "分类名称已存在"),
    COLUMN_NAME_ALREADY_EXISTS(20002, "该分类下已存在同名专栏");

    private final Integer code;
    private final String message;

    ErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
