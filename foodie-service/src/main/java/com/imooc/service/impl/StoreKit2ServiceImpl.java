package com.imooc.service.impl;

import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.HistoryResponse;
import com.apple.itunes.storekit.model.StatusResponse;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.model.TransactionHistoryRequest;
import com.apple.itunes.storekit.model.Data;
import com.apple.itunes.storekit.model.JWSTransactionDecodedPayload;
import com.apple.itunes.storekit.model.Type;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;
import com.imooc.mapper.IAPOrderMapper;
import com.imooc.pojo.IAPOrder;
import com.imooc.service.StoreKit2Service;
import com.imooc.service.config.AppStoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public class StoreKit2ServiceImpl implements StoreKit2Service {

    // 定义交易状态码
    private static final int STATUS_PURCHASED = 1;  // 已购买
    private static final int STATUS_RENEWED = 2;    // 已续订
    private static final int STATUS_EXPIRED = 3;    // 已过期
    private static final int STATUS_REFUNDED = 4;   // 已退款
    private static final int STATUS_REVOKED = 5;    // 已撤销

    @Autowired
    private AppStoreConfig appStoreConfig;

    @Autowired
    private IAPOrderMapper iapOrderMapper;

    private AppStoreServerAPIClient apiClient;
    private SignedDataVerifier signedDataVerifier;

    @PostConstruct
    public void init() {
        Environment environment = appStoreConfig.isEnvironment() ? Environment.PRODUCTION : Environment.SANDBOX;
        
        // 初始化 API 客户端
        apiClient = new AppStoreServerAPIClient(
            appStoreConfig.getPrivateKey(),
            appStoreConfig.getKeyId(),
            appStoreConfig.getIssuerId(),
            appStoreConfig.getBundleId(),
            environment
        );
        
        // 初始化签名验证器
        signedDataVerifier = new SignedDataVerifier(
            appStoreConfig.getRootCertificates(),
            appStoreConfig.getBundleId(),
            appStoreConfig.getAppAppleId(),
            environment,
            true  // 启用在线检查
        );
    }

    private int convertTypeToStatus(Type type) {
        if (type == null) {
            return STATUS_PURCHASED;  // 默认状态
        }
        
        switch (type) {
            case AUTO_RENEWABLE_SUBSCRIPTION:
            case NON_CONSUMABLE:
            case CONSUMABLE:
                return STATUS_PURCHASED;
            case NON_RENEWING_SUBSCRIPTION:
                return STATUS_RENEWED;
            default:
                return STATUS_PURCHASED;
        }
    }

    @Override
    public IAPOrder verifyTransaction(String signedPayload) {
        try {
            // 验证签名
            ResponseBodyV2DecodedPayload payload = signedDataVerifier.verifyAndDecodeNotification(signedPayload);
            
            if (payload == null) {
                throw new RuntimeException("Invalid transaction signature");
            }
            
            Data data = payload.getData();
            if (data == null || data.getSignedTransactionInfo() == null) {
                throw new RuntimeException("No transaction data found");
            }
            
            // 解码交易信息
            JWSTransactionDecodedPayload transactionInfo = signedDataVerifier.verifyAndDecodeTransaction(data.getSignedTransactionInfo());
            
            if (transactionInfo == null) {
                throw new RuntimeException("Invalid transaction info");
            }
            
            // 获取交易历史
            TransactionHistoryRequest request = new TransactionHistoryRequest()
                .sort(TransactionHistoryRequest.Order.DESCENDING)
                .revoked(false);
                
            HistoryResponse response = apiClient.getTransactionHistory(
                transactionInfo.getOriginalTransactionId(),
                null,
                request,
                null
            );
            
            // 保存交易信息
            IAPOrder iapOrder = new IAPOrder();
            iapOrder.setTransactionId(transactionInfo.getTransactionId());
            iapOrder.setProductId(transactionInfo.getProductId());
            iapOrder.setEnvironment(appStoreConfig.isEnvironment() ? "Production" : "Sandbox");
            iapOrder.setStatus(convertTypeToStatus(transactionInfo.getType()));
            iapOrder.setPurchaseDate(new Date(transactionInfo.getPurchaseDate()));
            iapOrder.setVerifyDate(new Date());
            
            iapOrderMapper.insert(iapOrder);
            
            log.info("Successfully verified transaction: {}", transactionInfo.getTransactionId());
            return iapOrder;
            
        } catch (VerificationException | APIException | IOException e) {
            log.error("Error verifying transaction: ", e);
            throw new RuntimeException("Failed to verify transaction", e);
        }
    }

    @Override
    public void handleServerNotification(String signedPayload) {
        try {
            // 验证通知签名
            ResponseBodyV2DecodedPayload payload = signedDataVerifier.verifyAndDecodeNotification(signedPayload);
            
            if (payload == null) {
                throw new RuntimeException("Invalid notification signature");
            }
            
            Data data = payload.getData();
            if (data == null || data.getSignedTransactionInfo() == null) {
                throw new RuntimeException("No transaction data found");
            }
            
            // 解码交易信息
            JWSTransactionDecodedPayload transactionInfo = signedDataVerifier.verifyAndDecodeTransaction(data.getSignedTransactionInfo());
            
            if (transactionInfo == null) {
                throw new RuntimeException("Invalid transaction info");
            }
            
            // 根据通知类型处理
            switch (payload.getNotificationType()) {
                case SUBSCRIBED:
                case DID_RENEW:
                    handleSubscriptionRenewal(transactionInfo);
                    break;
                case DID_FAIL_TO_RENEW:
                case EXPIRED:
                    handleSubscriptionExpiration(transactionInfo);
                    break;
                case REFUND:
                    handleRefund(transactionInfo);
                    break;
                default:
                    log.info("Received notification of type: {}", payload.getNotificationType());
            }
            
        } catch (VerificationException e) {
            log.error("Error handling server notification: ", e);
            throw new RuntimeException("Failed to handle server notification", e);
        }
    }

    @Override
    public IAPOrder getTransactionHistory(String originalTransactionId) {
        try {
            TransactionHistoryRequest request = new TransactionHistoryRequest()
                .sort(TransactionHistoryRequest.Order.DESCENDING)
                .revoked(false);
                
            HistoryResponse response = apiClient.getTransactionHistory(
                originalTransactionId,
                null,
                request,
                null
            );
            
            // 获取最新的交易记录
            ResponseBodyV2DecodedPayload latestTransaction = signedDataVerifier.verifyAndDecodeNotification(
                response.getSignedTransactions().get(0)
            );
            
            Data data = latestTransaction.getData();
            if (data == null || data.getSignedTransactionInfo() == null) {
                throw new RuntimeException("No transaction data found");
            }
            
            // 解码交易信息
            JWSTransactionDecodedPayload transactionInfo = signedDataVerifier.verifyAndDecodeTransaction(data.getSignedTransactionInfo());
            
            if (transactionInfo == null) {
                throw new RuntimeException("Invalid transaction info");
            }
            
            // 更新订单状态
            IAPOrder iapOrder = new IAPOrder();
            iapOrder.setTransactionId(transactionInfo.getTransactionId());
            iapOrder.setStatus(convertTypeToStatus(transactionInfo.getType()));
            iapOrder.setVerifyDate(new Date());
            
            iapOrderMapper.updateById(iapOrder);
            
            return iapOrder;
            
        } catch (VerificationException | APIException | IOException e) {
            log.error("Error getting transaction history: ", e);
            throw new RuntimeException("Failed to get transaction history", e);
        }
    }

    @Override
    public IAPOrder getSubscriptionStatus(String originalTransactionId) {
        try {
            // 获取所有订阅状态
            StatusResponse response = apiClient.getAllSubscriptionStatuses(
                originalTransactionId,
                null  // 不过滤状态
            );
            
            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                throw new RuntimeException("No subscription data found");
            }
            
            // 获取第一个订阅组的最新交易
            var subscriptionGroup = response.getData().get(0);
            if (subscriptionGroup.getLastTransactions() == null || subscriptionGroup.getLastTransactions().isEmpty()) {
                throw new RuntimeException("No transaction data found");
            }
            
            // 获取最新的交易信息
            var lastTransaction = subscriptionGroup.getLastTransactions().get(0);
            if (lastTransaction.getSignedTransactionInfo() == null) {
                throw new RuntimeException("No signed transaction info found");
            }
            
            // 解码交易信息
            JWSTransactionDecodedPayload transactionInfo = signedDataVerifier.verifyAndDecodeTransaction(
                lastTransaction.getSignedTransactionInfo()
            );
            
            if (transactionInfo == null) {
                throw new RuntimeException("Invalid transaction info");
            }
            
            // 更新订单状态
            IAPOrder iapOrder = new IAPOrder();
            iapOrder.setTransactionId(originalTransactionId);
            iapOrder.setStatus(convertTypeToStatus(transactionInfo.getType()));
            iapOrder.setVerifyDate(new Date());
            
            iapOrderMapper.updateById(iapOrder);
            
            return iapOrder;
            
        } catch (VerificationException | APIException | IOException e) {
            log.error("Error getting subscription status: ", e);
            throw new RuntimeException("Failed to get subscription status", e);
        }
    }

    private void handleSubscriptionRenewal(JWSTransactionDecodedPayload transactionInfo) {
        // 处理订阅更新
        IAPOrder iapOrder = new IAPOrder();
        iapOrder.setTransactionId(transactionInfo.getTransactionId());
        iapOrder.setStatus(STATUS_RENEWED);
        iapOrder.setVerifyDate(new Date());
        
        iapOrderMapper.updateById(iapOrder);
    }

    private void handleSubscriptionExpiration(JWSTransactionDecodedPayload transactionInfo) {
        // 处理订阅过期
        IAPOrder iapOrder = new IAPOrder();
        iapOrder.setTransactionId(transactionInfo.getTransactionId());
        iapOrder.setStatus(STATUS_EXPIRED);
        iapOrder.setVerifyDate(new Date());
        
        iapOrderMapper.updateById(iapOrder);
    }

    private void handleRefund(JWSTransactionDecodedPayload transactionInfo) {
        // 处理退款
        IAPOrder iapOrder = new IAPOrder();
        iapOrder.setTransactionId(transactionInfo.getTransactionId());
        iapOrder.setStatus(STATUS_REFUNDED);
        iapOrder.setVerifyDate(new Date());
        
        iapOrderMapper.updateById(iapOrder);
    }
}
