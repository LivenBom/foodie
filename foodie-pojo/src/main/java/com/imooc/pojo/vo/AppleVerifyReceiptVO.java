package com.imooc.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AppleVerifyReceiptVO {
    private int status;                   // 0 if valid
    private Environment environment;       // Sandbox or Production
    @JsonProperty("latest_receipt")
    private String latestReceipt;         // Latest base64 encoded receipt
    @JsonProperty("latest_receipt_info")
    private List<ReceiptInfo> latestReceiptInfo;

    @Data
    public static class ReceiptInfo {
        @JsonProperty("transaction_id")
        private String transactionId;
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("purchase_date_ms")
        private String purchaseDateMs;
        @JsonProperty("original_transaction_id")
        private String originalTransactionId;
    }

    public enum Environment {
        Sandbox,
        Production
    }
}
