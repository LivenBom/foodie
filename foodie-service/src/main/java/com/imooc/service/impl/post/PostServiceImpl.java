package com.imooc.service.impl.post;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Post;
import com.imooc.pojo.vo.post.*;
import com.imooc.service.post.PostService;
import com.imooc.mapper.PostMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.imooc.pojo.Post;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.Date;

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ArticleDetailVO queryArticleDetailById(String id) {
        Post post = baseMapper.selectById(id);
        if (post == null) {
            return null;
        }
        ArticleDetailVO result = new ArticleDetailVO();
        BeanUtils.copyProperties(post, result);
        return result;
    }

    @Override
    public String createPost(CreatePostReqVO createPostReqVO) {
        // 1. 创建文章实体
        Post post = new Post();
        post.setId(UUID.randomUUID().toString()); // 生成唯一ID
        post.setTitle(createPostReqVO.getTitle());
        post.setContent(createPostReqVO.getContent());
        post.setComments(0);
        post.setCollects(0);
        post.setView(0);
        post.setTop(0);  // 默认不置顶
        post.setTopicId(createPostReqVO.getTopicId());

        // 2. 保存文章
        save(post);

        return post.getId();
    }
}
