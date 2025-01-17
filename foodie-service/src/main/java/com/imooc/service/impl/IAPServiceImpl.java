package com.imooc.service.impl;

import com.imooc.pojo.bo.IAPReceiptBO;
import com.imooc.pojo.vo.AppleVerifyReceiptVO;
import com.imooc.service.IAPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class IAPServiceImpl implements IAPService {

    private static final String SANDBOX_VERIFY_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static final String PROD_VERIFY_URL = "https://buy.itunes.apple.com/verifyReceipt";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AppleVerifyReceiptVO verifyReceipt(IAPReceiptBO receiptBO) {
        String verifyUrl = "sandbox".equalsIgnoreCase(receiptBO.getEnvironment()) 
            ? SANDBOX_VERIFY_URL 
            : PROD_VERIFY_URL;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("receipt-data", receiptBO.getReceiptData());

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            AppleVerifyReceiptVO response = restTemplate.postForObject(
                verifyUrl,
                request,
                AppleVerifyReceiptVO.class
            );

            log.info("Apple IAP verification response for transaction {}: {}", 
                receiptBO.getTransactionId(), response);

            return response;
        } catch (Exception e) {
            log.error("Error verifying Apple IAP receipt: ", e);
            throw new RuntimeException("Failed to verify Apple receipt", e);
        }
    }
}
