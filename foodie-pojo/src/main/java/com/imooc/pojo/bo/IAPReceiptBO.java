package com.imooc.pojo.bo;

import lombok.Data;

@Data
public class IAPReceiptBO {
    private String receiptData;           // Base64 编码的收据数据
    private String transactionId;         // Apple 交易 ID
    private String originalTransactionId; // 原始交易 ID（用于订阅续期）
    private String productId;             // 产品标识符
    private String purchaseDate;          // 交易时间
    private String userId;                // 系统用户 ID
    private String environment;           // 环境：sandbox 或 production
}
