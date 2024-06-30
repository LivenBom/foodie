package com.imooc.pojo.vo.post;

import lombok.Data;

import java.util.List;

@Data
public class PostTopicsVO {
    private String topicId;
    private String topicTitle;
    private String topicIcon;
    private String columnId;

    private List<PostArticleVO> articleList;
}
