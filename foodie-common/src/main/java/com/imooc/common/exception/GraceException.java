package com.imooc.common.exception;

import com.imooc.common.enums.ErrorCodeEnum;

public class GraceException extends RuntimeException {

    private Integer code;

    public GraceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public static void display(ErrorCodeEnum errorCodeEnum) {
        throw new GraceException(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }
}
