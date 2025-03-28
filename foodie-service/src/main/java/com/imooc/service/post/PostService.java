package com.imooc.service.post;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.vo.post.ArticleDetailVO;
import com.imooc.pojo.vo.post.PostArticleVO;
import com.imooc.pojo.vo.post.PostCategoriesVO;
import com.imooc.pojo.Post;
import com.imooc.pojo.vo.post.PostTopicsVO;
import com.imooc.pojo.vo.post.CreatePostReqVO;

import java.util.List;

/**
* @author liven
* @description 针对表【post(文章)】的数据库操作Service
* @createDate 2024-06-30 12:57:06
*/
public interface PostService extends IService<Post> {

    /*
    * 获取所有的分类及其专栏
    * */
    public List<PostCategoriesVO> queryAllCategoriesWithColumns();

    /*
    * 根据专栏id，查询专栏下的所有Topic及文章
    * */
    public List<PostTopicsVO> queryTopicsWithArticlesByColumnId(Integer columnId);

    /*
    * 根据文章id，查询文章详情
    * */
    public ArticleDetailVO queryArticleDetailById(String id);

    /*
    * 创建新文章
    * */
    public String createPost(CreatePostReqVO createPostReqVO);

}
