package com.imooc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Sex {
    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    @EnumValue
    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
