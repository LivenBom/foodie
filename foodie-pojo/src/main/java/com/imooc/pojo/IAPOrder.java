package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("iap_order")
public class IAPOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String transactionId;
    private String receiptData;
    private Integer status;
    private String environment;
    private String productId;
    private Date purchaseDate;
    private Date verifyDate;
    private Date createTime;
    private Date updateTime;
}
