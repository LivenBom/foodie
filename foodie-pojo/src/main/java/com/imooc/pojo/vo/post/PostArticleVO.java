package com.imooc.pojo.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostArticleVO {
    private String articleId;
    private String articleTitle;
    private Integer isTop;
}
