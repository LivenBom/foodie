package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Items;
import com.imooc.service.ItemsService;
import com.imooc.mapper.ItemsMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【items(商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表)】的数据库操作Service实现
* @createDate 2024-06-01 22:56:19
*/
@Service
public class ItemsServiceImpl extends ServiceImpl<ItemsMapper, Items>
    implements ItemsService{

}




