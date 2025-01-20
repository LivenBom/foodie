package com.imooc.service.impl;

import com.imooc.pojo.bo.IAPReceiptBO;
import com.imooc.pojo.vo.AppleVerifyReceiptVO;
import com.imooc.service.IAPService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.mapper.IAPOrderMapper;
import com.imooc.pojo.IAPOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class IAPServiceImpl implements IAPService {

    private static final String SANDBOX_VERIFY_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static final String PROD_VERIFY_URL = "https://buy.itunes.apple.com/verifyReceipt";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IAPOrderMapper iapOrderMapper;

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

            // Save the verification result
            IAPOrder iapOrder = new IAPOrder();
            iapOrder.setTransactionId(receiptBO.getTransactionId());
            iapOrder.setReceiptData(receiptBO.getReceiptData());
            iapOrder.setStatus(response.getStatus());
            iapOrder.setEnvironment(receiptBO.getEnvironment());
            iapOrder.setProductId(receiptBO.getProductId());
            iapOrder.setVerifyDate(new Date());
            
            if (response.getStatus() == 0) {
                iapOrder.setPurchaseDate(new Date()); // You might want to get this from the receipt response
            }
            
            iapOrderMapper.insert(iapOrder);

            log.info("Apple IAP verification response for transaction {}: {}", 
                receiptBO.getTransactionId(), response);

            return response;
        } catch (Exception e) {
            log.error("Error verifying Apple IAP receipt: ", e);
            throw new RuntimeException("Failed to verify Apple receipt", e);
        }
    }

    @Override
    public IPage<IAPOrder> queryIAPOrders(String transactionId, String productId, String environment, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<IAPOrder> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.isNotBlank(transactionId)) {
            queryWrapper.like(IAPOrder::getTransactionId, transactionId);
        }
        if (StringUtils.isNotBlank(productId)) {
            queryWrapper.eq(IAPOrder::getProductId, productId);
        }
        if (StringUtils.isNotBlank(environment)) {
            queryWrapper.eq(IAPOrder::getEnvironment, environment);
        }
        
        queryWrapper.orderByDesc(IAPOrder::getCreateTime);
        
        return iapOrderMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public IAPOrder getIAPOrderByTransactionId(String transactionId) {
        LambdaQueryWrapper<IAPOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IAPOrder::getTransactionId, transactionId);
        return iapOrderMapper.selectOne(queryWrapper);
    }
}
