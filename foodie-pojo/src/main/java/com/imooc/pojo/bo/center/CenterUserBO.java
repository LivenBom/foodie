package com.imooc.pojo.bo.center;

import com.imooc.enums.Sex;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "用户昵称不能为空")
    @Size(max = 12, message = "用户昵称长度不能超过12位")
    private String nickname;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 12, message = "真实姓名长度不能超过12位")
    private String realname;

    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$", message = "手机号格式不正确")
    private String mobile;

    @Email
    private String email;

    private Sex sex;
    private Date birthday;
}
