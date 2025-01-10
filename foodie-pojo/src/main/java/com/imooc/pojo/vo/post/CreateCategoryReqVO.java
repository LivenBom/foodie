package com.imooc.pojo.vo.post;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
@Data
public class CreateCategoryReqVO {
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;
}
