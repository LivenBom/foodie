package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.imooc.enums.YesOrNo;
import lombok.Data;

/**
 * 商品图片 
 * @TableName items_img
 */
@TableName(value ="items_img")
@Data
public class ItemsImg implements Serializable {
    /**
     * 图片主键
     */
    @TableId
    private String id;

    /**
     * 商品外键id 商品外键id
     */
    private String itemId;

    /**
     * 图片地址 图片地址
     */
    private String url;

    /**
     * 顺序 图片顺序，从小到大
     */
    private Integer sort;

    /**
     * 是否主图 是否主图，1：是，0：否
     */
    @EnumValue
    private YesOrNo isMain;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}