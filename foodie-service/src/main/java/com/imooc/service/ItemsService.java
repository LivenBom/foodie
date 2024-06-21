package com.imooc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Items;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVO;
import com.imooc.pojo.vo.SearchItemVO;
import com.imooc.pojo.vo.ShopcarVO;

import java.util.List;

/**
* @author liven
* @description 针对表【items(商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表)】的数据库操作Service
* @createDate 2024-06-01 22:56:19
*/
public interface ItemsService extends IService<Items> {

    /*
    * 根据商品id查询商品详情
    * */
    public Items queryItemById(String itemId);


    /*
    * 查询商品图片列表
    * */
    public List<ItemsImg> queryItemImgList(String itemId);

    /*
    * 查询单个商品规格
    * */
    public ItemsSpec queryItemSpecById(String specId);

    /*
    * 查询商品规格
    * */
    public List<ItemsSpec> queryItemSpecList(String itemId);


    /*
    * 查询商品参数
    * */
    public ItemsParam queryItemParam(String itemId);


    /*
    * 查询商品的评价等级数量
    * */
    public CommentLevelCountsVO queryCommentCounts(String itemId);


    /*
    * 查询商品的评价（支持分页）
    * */
    public IPage<ItemCommentVO> queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);


    /*
    * 商品搜索
    * */
    public IPage<SearchItemVO> searchItems(String keywords, String sort, Integer page, Integer pageSize);


    /*
    * 根据分类id搜索商品列表
    * */
    public IPage<SearchItemVO> searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);


    /*
     * 根据规格ids查询最新购物车中商品数据（用于刷新渲染购物车中的商品数据）
     * */
    public List<ShopcarVO> queryItemsBySpecIds(String specIds);


    /*
    * 根据商品id获取商品图片主图url
    * */
    public String queryItemMainImgById(String itemId);


    /*
    * 减少库存
    * */
    public void decreaseItemSpecStock(String specId, int buyCounts);
}
