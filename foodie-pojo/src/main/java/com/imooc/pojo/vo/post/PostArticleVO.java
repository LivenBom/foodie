package com.imooc.pojo.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostArticleVO {
    private String id;
    private String title;
    private Integer isTop;
    private Integer isPaid;
}
