package com.imooc.pojo.bo;

import lombok.Data;

/*
* 用于接受客户端传来的数据封装成的对象都是 BO
* */
@Data
public class UserBO {
    private String username;
    private String password;
    private String confirmPassword;
}
