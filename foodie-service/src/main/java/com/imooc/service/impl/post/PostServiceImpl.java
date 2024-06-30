package com.imooc.service.impl.post;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Post;
import com.imooc.pojo.vo.post.PostArticleVO;
import com.imooc.pojo.vo.post.PostCategoriesVO;
import com.imooc.pojo.vo.post.PostTopicsVO;
import com.imooc.service.post.PostService;
import com.imooc.mapper.PostMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import com.imooc.pojo.Post;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author liven
* @description 针对表【post(文章)】的数据库操作Service实现
* @createDate 2024-06-30 12:57:06
*/
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
    implements PostService{

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<PostCategoriesVO> queryAllCategoriesWithColumns() {
        return baseMapper.queryAllCategoriesWithColumns();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<PostTopicsVO> queryTopicsWithArticlesByColumnId(Integer columnId) {
        return baseMapper.queryTopicsWithArticlesByColumnId(columnId);
    }
}




