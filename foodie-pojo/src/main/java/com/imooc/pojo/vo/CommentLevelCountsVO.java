package com.imooc.pojo.vo;

import lombok.Data;

/*
* 用于展示商品评价数的VO
* */
@Data
public class CommentLevelCountsVO {
    private Long totalCounts;
    private Long goodCounts;
    private Long normalCounts;
    private Long badCounts;
}

