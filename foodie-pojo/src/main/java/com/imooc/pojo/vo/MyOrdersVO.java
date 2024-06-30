package com.imooc.pojo.vo;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MyOrdersVO {
    private String orderId;
    private Date createdTime;
    private PayMethod payMethod;
    private Integer realPayAmount;
    private Integer postAmount;
    private Integer isComment;
    private OrderStatusEnum orderStatus;

    private List<MySubOrderItemVO> subOrderItemVOList;
}
