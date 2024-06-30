package com.imooc.pojo.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostCategoriesVO {
    private String categoryId;
    private String categoryName;

    private List<PostColumnVO> columnList;
}
