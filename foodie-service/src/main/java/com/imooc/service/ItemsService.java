package com.imooc.service;

import com.imooc.pojo.Items;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;

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
    * 查询商品规格
    * */
    public List<ItemsSpec> queryItemSpecList(String itemId);


    /*
    * 查询商品参数
    * */
    public ItemsParam queryItemParam(String itemId);

}
