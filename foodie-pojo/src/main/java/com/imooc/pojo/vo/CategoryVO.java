package com.imooc.pojo.vo;


import lombok.Data;
import java.util.List;

/*
* VO: View Object 视图对象
* 后台返回给前端的对象
* */

/*
* 二级分类VO
* */
@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    // 三级分类VO List
    private List<SubCategoryVO> subCatList;
}
