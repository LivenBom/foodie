package com.imooc.service;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.vo.CategoryVO;

import java.util.List;

/**
* @author liven
* @description 针对表【category(商品分类 )】的数据库操作Service
* @createDate 2024-06-01 22:56:19
*/
public interface CategoryService extends IService<Category> {

    /*n
     * 查询所有一级分类
     * */
    public List<Category> queryAllRootLevelCat();


    /*
    * 根据一级分类id查询子分类信息
    * */
    public List<CategoryVO> getSubCatList(Integer rootCatId);
}
