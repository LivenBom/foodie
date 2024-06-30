package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 文章话题
 * @TableName post_topic
 */
@TableName(value ="post_topic")
@Data
public class PostTopic implements Serializable {
    /**
     * 话题id
     */
    @TableId
    private Integer id;

    /**
     * 话题标题
     */
    private String title;

    /**
     * 话题icon
     */
    private String icon;

    /**
     * 专栏id
     */
    private Integer columnId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}