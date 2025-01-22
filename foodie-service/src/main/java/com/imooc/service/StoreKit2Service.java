package com.imooc.service;

import com.imooc.pojo.IAPOrder;

public interface StoreKit2Service {
    /**
     * 验证交易
     * @param signedPayload JWS签名的交易信息
     * @return 验证结果
     */
    IAPOrder verifyTransaction(String signedPayload);
    
    /**
     * 处理App Store服务器通知
     * @param signedPayload JWS签名的通知信息
     */
    void handleServerNotification(String signedPayload);
    
    /**
     * 查询交易历史
     * @param originalTransactionId 原始交易ID
     * @return 交易历史
     */
    IAPOrder getTransactionHistory(String originalTransactionId);
    
    /**
     * 查询订阅状态
     * @param originalTransactionId 原始交易ID
     * @return 订阅状态
     */
    IAPOrder getSubscriptionStatus(String originalTransactionId);
}
