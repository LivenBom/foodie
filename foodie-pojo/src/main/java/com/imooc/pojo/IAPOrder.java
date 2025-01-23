package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("iap_orders")
public class IAPOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String userId;
    private String transactionId;
    private String originalTransactionId;
    private String productId;
    private Integer status;
    private String environment;
    private Date purchaseDate;
    private Date expiresDate;
    private BigDecimal priceAmount;
    private String currency;
    private Integer quantity;
    private Date verifyDate;
    private Date createTime;
    private Date updateTime;
}
