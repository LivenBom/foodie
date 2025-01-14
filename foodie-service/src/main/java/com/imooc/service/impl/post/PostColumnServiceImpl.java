package com.imooc.service.impl.post;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.imooc.common.enums.ErrorCodeEnum;
import com.imooc.common.exception.GraceException;
import com.imooc.mapper.PostColumnMapper;
import com.imooc.pojo.PostColumn;
import com.imooc.pojo.vo.post.CreateColumnReqVO;
import com.imooc.service.post.PostColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostColumnServiceImpl implements PostColumnService {

    @Autowired
    private PostColumnMapper postColumnMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Integer createColumn(CreateColumnReqVO createColumnReqVO) {
        // 1. 检查专栏名称是否已存在
        LambdaQueryWrapper<PostColumn> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PostColumn::getName, createColumnReqVO.getName())
                   .eq(PostColumn::getCategoryId, createColumnReqVO.getCategoryId());
        Long count = postColumnMapper.selectCount(queryWrapper);
        if (count > 0) {
            GraceException.display(ErrorCodeEnum.COLUMN_NAME_ALREADY_EXISTS);
        }

        // 2. 创建新专栏
        PostColumn column = new PostColumn();
        column.setName(createColumnReqVO.getName());
        column.setCategoryId(createColumnReqVO.getCategoryId());
        column.setIcon(createColumnReqVO.getIcon());
        column.setCollapseEnable(createColumnReqVO.getCollapseEnable());
        column.setSort(0); // 默认排序值为0
        
        postColumnMapper.insert(column);
        return column.getId();
    }
}
