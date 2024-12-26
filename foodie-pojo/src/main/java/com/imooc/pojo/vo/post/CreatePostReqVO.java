package com.imooc.pojo.vo.post;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建文章请求VO
 */
@Data
public class CreatePostReqVO {
    /**
     * 标题
     */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /**
     * markdown内容
     */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 主题ID
     */
    @NotNull(message = "主题ID不能为空")
    private Integer topicId;
}
