package com.imooc.controller.post;

import com.imooc.pojo.vo.post.CreateCategoryReqVO;
import com.imooc.service.post.PostCategoryService;
import com.imooc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 文章分类相关接口
 */
@RestController
@RequestMapping("post/category")
public class PostCategoryController {

    @Autowired
    private PostCategoryService postCategoryService;

    /**
     * 创建新分类
     */
    @PostMapping("/create")
    public IMOOCJSONResult createCategory(@RequestBody CreateCategoryReqVO createCategoryReqVO) {
        // 创建分类
        Integer categoryId = postCategoryService.createCategory(createCategoryReqVO);
        return IMOOCJSONResult.ok(categoryId);
    }
}
