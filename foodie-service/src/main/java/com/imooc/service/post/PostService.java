package com.imooc.service.post;

import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.vo.post.PostCategoriesVO;
import com.imooc.pojo.Post;

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
}
