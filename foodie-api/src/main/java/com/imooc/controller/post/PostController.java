package com.imooc.controller.post;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.post.PostCategoriesVO;
import com.imooc.pojo.vo.post.PostTopicsVO;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUsersService;
import com.imooc.service.post.PostService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

@Tags({@Tag(name = "文章相关接口")})
@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/queryAllCategoriesWithColumns")
    public IMOOCJSONResult queryAllCategoriesWithColumns() {
        List<PostCategoriesVO> categories = postService.queryAllCategoriesWithColumns();
        return IMOOCJSONResult.ok(categories);
    }

    @GetMapping("/queryArticlesByColumnId")
    public IMOOCJSONResult queryArticlesByColumnId(@RequestParam Integer columnId) {
        if (columnId == null) {
            return IMOOCJSONResult.errorMsg("栏目id不能为空");
        }
        List<PostTopicsVO> articles = postService.queryTopicsWithArticlesByColumnId(columnId);
        return IMOOCJSONResult.ok(articles);
    }
}
