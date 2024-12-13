package com.imooc.pojo.bo;

import lombok.Data;

/**
 * 用于登录接口的业务对象
 */
@Data
public class UserBO {
    private String username;
    private String password;
}
