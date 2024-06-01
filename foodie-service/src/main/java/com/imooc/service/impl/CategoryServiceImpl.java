package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Category;
import com.imooc.service.CategoryService;
import com.imooc.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【category(商品分类 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:19
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




