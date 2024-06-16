package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.CommentLevel;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemVO;
import com.imooc.service.ItemsService;
import com.imooc.utils.DesensitizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public IPage<ItemCommentVO> queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);

        // page 是当前的页码，pageSize 是每页显示的条数
        Page<ItemCommentVO> pageItem = new Page<>(page, pageSize);
        Page<ItemCommentVO> results = baseMapper.queryItemComments(pageItem, map);
        // 脱敏设置（隐私保护，不显示全称）
        results.getRecords().forEach(vo -> {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        });
        return results;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public IPage<SearchItemVO> searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);

        Page<SearchItemVO> pageItem = new Page<>(page, pageSize);
        return baseMapper.searchItems(pageItem, map);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public IPage<SearchItemVO> searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);

        Page<SearchItemVO> pageItem = new Page<>(page, pageSize);
        return baseMapper.searchItemsByThirdCat(pageItem, map);
    }
}




