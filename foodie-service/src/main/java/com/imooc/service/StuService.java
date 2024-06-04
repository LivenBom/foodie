package com.imooc.service;

import com.imooc.pojo.Stu;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liven
* @description 针对表【stu】的数据库操作Service
* @createDate 2024-06-01 22:56:20
*/
public interface StuService extends IService<Stu> {
    public Stu getStuInfo(String id);
}
