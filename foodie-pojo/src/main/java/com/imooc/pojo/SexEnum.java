package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum SexEnum {
    MALE(0, "男"),
    FEMALE(1, "女"),
    SECRET(2, "保密");

    @EnumValue
    private Integer sex;
    private String sexName;

    SexEnum(int sex, String name) {
        this.sex = sex;
        this.sexName = name;
    }
}
