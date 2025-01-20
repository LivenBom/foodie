package com.imooc.pojo.bo;

import lombok.Data;

@Data
public class OrderQueryBO {
    private Integer page = 1;
    private Integer pageSize = 10;
    private String orderId;
    private String userId;
    private Integer status;
}
