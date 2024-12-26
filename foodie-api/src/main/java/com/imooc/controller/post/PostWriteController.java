package com.imooc.controller.post;

import com.imooc.pojo.vo.post.CreatePostReqVO;
import com.imooc.service.post.PostService;
import com.imooc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章写作相关接口
 */
@RestController
@RequestMapping("post/write")
public class PostWriteController {

    @Autowired
    private PostService postService;

    /**
     * 创建新文章
     */
    @PostMapping("/create")
    public IMOOCJSONResult createPost(@RequestBody @Validated CreatePostReqVO createPostReqVO) {
        // 创建文章
        String postId = postService.createPost(createPostReqVO);
        return IMOOCJSONResult.ok(postId);
    }
}
