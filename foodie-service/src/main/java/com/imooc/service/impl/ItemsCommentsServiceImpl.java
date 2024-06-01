package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.ItemsComments;
import com.imooc.service.ItemsCommentsService;
import com.imooc.mapper.ItemsCommentsMapper;
import org.springframework.stereotype.Service;

/**
* @author liven
* @description 针对表【items_comments(商品评价表 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:19
*/
@Service
public class ItemsCommentsServiceImpl extends ServiceImpl<ItemsCommentsMapper, ItemsComments>
    implements ItemsCommentsService{

}




