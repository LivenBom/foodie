package com.imooc.pojo.vo.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateColumnReqVO {
    
    @NotBlank(message = "专栏名称不能为空")
    private String name;
    
    @NotNull(message = "所属分类不能为空")
    private Integer categoryId;
    
    private String icon;
    
    private Integer collapseEnable;
}
