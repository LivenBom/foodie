package com.imooc.service.post;

import com.imooc.pojo.vo.post.CreateColumnReqVO;

public interface PostColumnService {
    
    /**
     * 创建新专栏
     * @param createColumnReqVO 创建专栏请求
     * @return 专栏ID
     */
    Integer createColumn(CreateColumnReqVO createColumnReqVO);
}
