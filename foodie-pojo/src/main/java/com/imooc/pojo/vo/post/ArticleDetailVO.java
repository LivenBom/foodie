package com.imooc.pojo.vo.post;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文章
 * @TableName post
 */
@Data
public class ArticleDetailVO implements Serializable {
    private String id;
    private String title;
    private String content;
    private String userId;
    private Integer comments;
    private Integer collects;
    private Integer view;
    private Integer top;
    private Integer topicId;
    private Date createdTime;
    private Date updatedTime;
}