package com.imooc.controller.post;

import com.imooc.pojo.vo.post.CreateColumnReqVO;
import com.imooc.service.post.PostColumnService;
import com.imooc.utils.IMOOCJSONResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/column")
public class PostColumnController {

    @Autowired
    private PostColumnService postColumnService;

    @PostMapping("/create")
    public IMOOCJSONResult createColumn(@RequestBody @Valid CreateColumnReqVO createColumnReqVO) {
        Integer columnId = postColumnService.createColumn(createColumnReqVO);
        return IMOOCJSONResult.ok(columnId);
    }
}
