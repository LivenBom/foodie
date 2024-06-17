package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class ShopcarVO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;
}
