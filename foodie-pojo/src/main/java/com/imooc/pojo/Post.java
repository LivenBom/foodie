package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 文章
 * @TableName post
 */
@TableName(value ="post")
@Data
public class Post implements Serializable {
    /**
     * 文章id
     */
    @TableId
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * markdown内容
     */
    private String content;

    /**
     * 作者id
     */
    private String userId;

    /**
     * 评论统计
     */
    private Integer comments;

    /**
     * 收藏统计
     */
    private Integer collects;

    /**
     * 浏览统计
     */
    private Integer view;

    /**
     * 是否置顶（1-是，0-否）
     */
    private Integer top;

    /**
     * 专题id
     */
    private Integer topicId;

    /**
     * 是否付费阅读（1-是，0-否）
     */
    @TableField("is_paid")
    private Integer isPaid;

    /**
     * 逻辑删除(1-删除，0-保留)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}