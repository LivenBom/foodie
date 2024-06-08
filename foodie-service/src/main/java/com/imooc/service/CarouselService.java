package com.imooc.service;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author liven
* @description 针对表【carousel(轮播图 )】的数据库操作Service
* @createDate 2024-06-01 22:56:19
*/
public interface CarouselService extends IService<Carousel> {

    /*
    * 查询所有轮播图列表
    * */
    public List<Carousel> queryAll(YesOrNo isShow);

}
