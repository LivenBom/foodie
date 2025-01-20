package com.imooc.controller;

import com.imooc.pojo.bo.IAPReceiptBO;
import com.imooc.pojo.vo.AppleVerifyReceiptVO;
import com.imooc.service.IAPService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tags({ @Tag(name = "内购") })
@RestController
@RequestMapping("iap")
public class IAPController {

    @Autowired
    private IAPService iapService;

    @PostMapping("/verify")
    public IMOOCJSONResult verifyReceipt(@RequestBody IAPReceiptBO receiptBO) {
        AppleVerifyReceiptVO result = iapService.verifyReceipt(receiptBO);
        
        if (result.getStatus() == 0) {
            return IMOOCJSONResult.ok(result);
        } else {
            return IMOOCJSONResult.errorMsg("Receipt verification failed with status: " + result.getStatus());
        }
    }

    @GetMapping("/orders")
    public IMOOCJSONResult queryOrders(
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String productId,
            @RequestParam(required = false) String environment,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return IMOOCJSONResult.ok(iapService.queryIAPOrders(transactionId, productId, environment, pageNum, pageSize));
    }

    @GetMapping("/orders/{transactionId}")
    public IMOOCJSONResult getOrder(@PathVariable String transactionId) {
        return IMOOCJSONResult.ok(iapService.getIAPOrderByTransactionId(transactionId));
    }
}
