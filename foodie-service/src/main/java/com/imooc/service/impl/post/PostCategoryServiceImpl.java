package com.imooc.service.impl.post;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.imooc.common.enums.ErrorCodeEnum;
import com.imooc.common.exception.GraceException;
import com.imooc.mapper.PostCategoryMapper;
import com.imooc.pojo.PostCategory;
import com.imooc.pojo.vo.post.CreateCategoryReqVO;
import com.imooc.service.post.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostCategoryServiceImpl implements PostCategoryService {

    @Autowired
    private PostCategoryMapper postCategoryMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createCategory(CreateCategoryReqVO createCategoryReqVO) {
        // 1. 检查分类名称是否已存在
        LambdaQueryWrapper<PostCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostCategory::getName, createCategoryReqVO.getName());
        Long count = postCategoryMapper.selectCount(queryWrapper);
        if (count > 0) {
            GraceException.display(ErrorCodeEnum.CATEGORY_NAME_ALREADY_EXISTS);
        }

        // 2. 创建新分类
        PostCategory category = new PostCategory();
        category.setName(createCategoryReqVO.getName());
        category.setIsShow(1); // 默认显示
        category.setSort(0); // 默认排序值为0
        
        postCategoryMapper.insert(category);
        return category.getId();
    }
}
