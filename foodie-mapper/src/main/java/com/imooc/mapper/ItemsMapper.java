package com.imooc.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.pojo.Items;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imooc.pojo.vo.ItemCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author liven
* @description 针对表【items(商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表)】的数据库操作Mapper
* @createDate 2024-06-01 22:56:19
* @Entity com.imooc.pojo.Items
*/
public interface ItemsMapper extends BaseMapper<Items> {

    public Page<ItemCommentVO> queryItemComments(Page<ItemCommentVO> page,
                                                 @Param("paramsMap") Map<String, Object> map);
}




