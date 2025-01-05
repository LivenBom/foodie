package com.imooc.pojo.bo;

import com.imooc.enums.Sex;
import lombok.Data;

/**
 * Apple 登录用户的业务对象
 */
@Data
public class AppleUserBO {
    private String id;
    private String username;
    private String appleId;
    private String email;
    private Sex sex;
    private String face;
}
