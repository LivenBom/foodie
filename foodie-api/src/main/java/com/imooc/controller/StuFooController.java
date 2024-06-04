package com.imooc.controller;

import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StuFooController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStu")
    public Object getStu(String id) {
        return stuService.getStuInfo(id);
    }

    @PostMapping("/saveStu")
    public Object saveStu(String name, Integer age) {
        Stu stu = new Stu();
        stu.setName(name);
        stu.setAge(age);
        return stuService.save(stu);
    }

    @PostMapping("/updateStu")
    public Object updateStu(Integer id, String name) {
        Stu stu = new Stu();
        stu.setId(id);
        stu.setName(name);
        return stuService.updateById(stu);
    }

    @PostMapping("/deleteStu")
    public Object deleteStu(String id) {
        return stuService.removeById(id);
    }
}
