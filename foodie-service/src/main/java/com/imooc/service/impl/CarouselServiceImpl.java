package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.service.CarouselService;
import com.imooc.mapper.CarouselMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liven
* @description 针对表【carousel(轮播图 )】的数据库操作Service实现
* @createDate 2024-06-01 22:56:19
*/
@Service
public class CarouselServiceImpl extends ServiceImpl<CarouselMapper, Carousel>
    implements CarouselService{

    @Override
    public List<Carousel> queryAll(YesOrNo isShow) {
        LambdaQueryWrapper<Carousel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Carousel::getIsShow, isShow);
        queryWrapper.orderByAsc(Carousel::getSort);
        return baseMapper.selectList(queryWrapper);
    }
}




