package com.imooc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/*
* 商品评价等级 枚举
* */
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    @EnumValue
    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}