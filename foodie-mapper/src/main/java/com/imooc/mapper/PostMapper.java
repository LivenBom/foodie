package com.imooc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.Post;
import com.imooc.pojo.vo.post.PostCategoriesVO;
import com.imooc.pojo.vo.post.PostTopicsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author liven
* @description 针对表【post(文章)】的数据库操作Mapper
* @createDate 2024-06-30 12:57:06
* @Entity com.imooc.pojo.Carousel.Post
*/
public interface PostMapper extends BaseMapper<Post> {

    public List<PostCategoriesVO> queryAllCategoriesWithColumns();

    public List<PostTopicsVO> queryTopicsWithArticlesByColumnId(@Param("columnId") Integer columnId);
}




