package com.imooc.pojo.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostTopicsVO {
    private String id;
    private String title;
    private String icon;
    private String columnId;

    private List<PostArticleVO> articleList;
}
