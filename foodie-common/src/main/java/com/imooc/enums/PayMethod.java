package com.imooc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum PayMethod {
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    @EnumValue
    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
