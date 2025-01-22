package com.imooc.controller;

import com.imooc.pojo.IAPOrder;
import com.imooc.service.StoreKit2Service;
import com.imooc.utils.IMOOCJSONResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/storekit2")
public class StoreKit2Controller {

    @Autowired
    private StoreKit2Service storeKit2Service;

    @PostMapping("/verify")
    public IMOOCJSONResult verifyTransaction(@RequestBody String signedPayload) {
        try {
            IAPOrder order = storeKit2Service.verifyTransaction(signedPayload);
            return IMOOCJSONResult.ok(order);
        } catch (Exception e) {
            log.error("Error verifying transaction: ", e);
            return IMOOCJSONResult.errorMsg("Failed to verify transaction");
        }
    }

    @PostMapping("/notification")
    public IMOOCJSONResult handleNotification(@RequestBody String signedPayload) {
        try {
            storeKit2Service.handleServerNotification(signedPayload);
            return IMOOCJSONResult.ok();
        } catch (Exception e) {
            log.error("Error handling notification: ", e);
            return IMOOCJSONResult.errorMsg("Failed to handle notification");
        }
    }

    @GetMapping("/transaction/{originalTransactionId}")
    public IMOOCJSONResult getTransactionHistory(@PathVariable String originalTransactionId) {
        try {
            IAPOrder order = storeKit2Service.getTransactionHistory(originalTransactionId);
            return IMOOCJSONResult.ok(order);
        } catch (Exception e) {
            log.error("Error getting transaction history: ", e);
            return IMOOCJSONResult.errorMsg("Failed to get transaction history");
        }
    }

    @GetMapping("/subscription/{originalTransactionId}")
    public IMOOCJSONResult getSubscriptionStatus(@PathVariable String originalTransactionId) {
        try {
            IAPOrder order = storeKit2Service.getSubscriptionStatus(originalTransactionId);
            return IMOOCJSONResult.ok(order);
        } catch (Exception e) {
            log.error("Error getting subscription status: ", e);
            return IMOOCJSONResult.errorMsg("Failed to get subscription status");
        }
    }
}
