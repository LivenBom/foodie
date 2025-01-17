package com.imooc.service;

import com.imooc.pojo.bo.IAPReceiptBO;
import com.imooc.pojo.vo.AppleVerifyReceiptVO;

public interface IAPService {
    /**
     * Verify Apple in-app purchase receipt
     * @param receiptBO Receipt data from iOS client
     * @return Verification result
     */
    AppleVerifyReceiptVO verifyReceipt(IAPReceiptBO receiptBO);
}
