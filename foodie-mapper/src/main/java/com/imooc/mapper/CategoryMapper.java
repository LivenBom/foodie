package com.imooc.mapper;

import com.imooc.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.vo.CategoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author liven
* @description 针对表【category(商品分类 )】的数据库操作Mapper
* @createDate 2024-06-01 22:56:19
* @Entity com.imooc.pojo.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {

    List<CategoryVO> getSubCatList(@Param("rootCatId")  Integer rootCatId);
}




