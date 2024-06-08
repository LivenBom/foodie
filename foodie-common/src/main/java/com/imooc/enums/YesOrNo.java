package com.imooc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum YesOrNo {
    NO(0, "否"),
    YES(1, "是");

    @EnumValue
    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}