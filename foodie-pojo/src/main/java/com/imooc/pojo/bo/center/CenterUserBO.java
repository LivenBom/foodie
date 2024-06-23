package com.imooc.pojo.bo.center;

import com.imooc.enums.Sex;
import lombok.Data;

import java.util.Date;

/*
* 用于接受客户端传来的数据封装成的对象都是 BO
* */
@Data
public class CenterUserBO {
    private String password;
    private String confirmPassword;
    private String username;
    private String nickName;
    private String realname;
    private String mobile;
    private String email;
    private Sex sex;
    private Date birthday;
}
