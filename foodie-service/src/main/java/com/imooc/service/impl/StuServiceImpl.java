package com.imooc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import com.imooc.mapper.StuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
* @author liven
* @description 针对表【stu】的数据库操作Service实现
* @createDate 2024-06-01 22:56:20
*/
@Service
public class StuServiceImpl extends ServiceImpl<StuMapper, Stu>
    implements StuService{

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(String id) {
        return this.getById(id);
    }
}




