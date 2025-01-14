package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章专栏
 * @TableName post_column
 */
@TableName(value ="post_column")
@Data
public class PostColumn implements Serializable {
    /**
     * 专栏id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 专栏名称
     */
    private String name;

    /**
     * 专栏icon
     */
    private String icon;

    /**
     * 是否可折叠（0,全部展示； 1，可以折叠显示）
     */
    @TableField("collapse_enable")
    private Integer collapseEnable;

    /**
     * 排序字段
     */
    private Integer sort;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}