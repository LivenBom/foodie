package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.UsersService;
import com.imooc.utils.IMOOCJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    /*
    * 轮播图列表查询
    * */
    @GetMapping("/carousel")
    public IMOOCJSONResult carousel() {
        return IMOOCJSONResult.ok(carouselService.queryAll(YesOrNo.YES));
    }


    /*
    * 一级分类列表查询
    * */
    @GetMapping("/cats")
    public IMOOCJSONResult rootCategories() {
        return IMOOCJSONResult.ok(categoryService.queryAllRootLevelCat());
    }

    /*
     * 根据一级分类id，获取所有子分类
     * */
    // 由于前端是通过路径参数传递的，所以这里使用@PathVariable
    // 路径参数，就是说在路径中传递参数，而没有给参数添加名字
    // 路径参数： serverUrl + '/index/subCat/' + rootCatId
    // 正常的Get： serverUrl + '/passport/usernameIsExist?username=' + username
    // 正常的Get这种方式可以不用@PathVariable，而是像Post一样用@RequestParam即可
    @GetMapping("/subCat/{rootCatId}")
    public IMOOCJSONResult subCat(@PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return IMOOCJSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return IMOOCJSONResult.ok(list);
    }

}
