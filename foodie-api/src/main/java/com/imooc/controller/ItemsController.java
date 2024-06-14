package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.CarouselService;
import com.imooc.service.CategoryService;
import com.imooc.service.ItemsService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tags({@Tag(name = "商品详情")})
@RestController
@RequestMapping("items")
public class ItemsController {
    @Autowired
    private ItemsService itemsService;


    @GetMapping("/info/{itemId}")
    public IMOOCJSONResult info(@PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        Items item = itemsService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemsService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemsService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemsService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);
        return IMOOCJSONResult.ok(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    public IMOOCJSONResult commentLevel(@RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        CommentLevelCountsVO countsVO = itemsService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(countsVO);
    }

}
