package com.imooc.pojo.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostCategoriesVO {
    private String id;
    private String title;

    private List<PostColumnVO> columnList;
}
