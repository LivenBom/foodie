package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章分类
 * @TableName post_category
 */
@TableName(value ="post_category")
@Data
public class PostCategory implements Serializable {
    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类icon
     */
    private String icon;

    /**
     * 显示状态，1表示显示，0表示不显示
     */
    @TableField("is_show")
    private Integer isShow;

    /**
     * 用于排序的字段
     */
    @TableField("sort")
    private Integer sort;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}