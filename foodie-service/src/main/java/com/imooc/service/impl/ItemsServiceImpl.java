package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.CommentLevel;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
* @author liven
* @description 针对表【items(商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表)】的数据库操作Service实现
* @createDate 2024-06-01 22:56:19
*/
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items>
    implements ItemsService{
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return baseMapper.selectById(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        LambdaQueryWrapper<ItemsImg> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ItemsImg::getItemId, itemId);
        return itemsImgMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        LambdaQueryWrapper<ItemsSpec> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ItemsSpec::getItemId, itemId);
        return itemsSpecMapper.selectList(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        LambdaQueryWrapper<ItemsParam> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ItemsParam::getItemId, itemId);
        return itemsParamMapper.selectOne(queryWrapper);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Long goodCounts = getCommentCounts(itemId, CommentLevel.GOOD);
        Long normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL);
        Long badCounts = getCommentCounts(itemId, CommentLevel.BAD);
        Long total = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setTotalCounts(total);
        commentLevelCountsVO.setGoodCounts(goodCounts);
        commentLevelCountsVO.setNormalCounts(normalCounts);
        commentLevelCountsVO.setBadCounts(badCounts);
        return commentLevelCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Long getCommentCounts(String itemId, CommentLevel level) {
        LambdaQueryWrapper<ItemsComments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ItemsComments::getItemId, itemId);
        queryWrapper.eq(level != null, ItemsComments::getCommentLevel, level.type);
        return itemsCommentsMapper.selectCount(queryWrapper);
    }
}




