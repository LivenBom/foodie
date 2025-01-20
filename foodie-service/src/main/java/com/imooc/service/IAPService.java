package com.imooc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.imooc.pojo.IAPOrder;
import com.imooc.pojo.bo.IAPReceiptBO;
import com.imooc.pojo.vo.AppleVerifyReceiptVO;

public interface IAPService {
    /**
     * Verify Apple in-app purchase receipt
     * @param receiptBO Receipt data from iOS client
     * @return Verification result
     */
    AppleVerifyReceiptVO verifyReceipt(IAPReceiptBO receiptBO);
    
    IPage<IAPOrder> queryIAPOrders(String transactionId, String productId, String environment, Integer pageNum, Integer pageSize);
    
    IAPOrder getIAPOrderByTransactionId(String transactionId);
}
