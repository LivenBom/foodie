package com.imooc.service.post;

import com.imooc.pojo.vo.post.CreateCategoryReqVO;

public interface PostCategoryService {
    /**
     * 创建新分类
     * @param createCategoryReqVO 创建分类请求
     * @return 分类ID
     */
    Integer createCategory(CreateCategoryReqVO createCategoryReqVO);
}
