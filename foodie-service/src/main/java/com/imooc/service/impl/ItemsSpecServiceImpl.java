package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.ItemsSpec;
import com.imooc.service.ItemsSpecService;
import com.imooc.mapper.ItemsSpecMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【items_spec(商品规格 每一件商品都有不同的规格，不同的规格又有不同的价格和优惠力度，规格表为此设计)】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class ItemsSpecServiceImpl extends ServiceImpl<ItemsSpecMapper, ItemsSpec>
    implements ItemsSpecService{

}




