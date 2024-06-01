package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.Getter;

@Data
public class Product {
    Long id;
    String name;
    Integer price;

    @Version // 标识乐观锁版本号（当然数据库也必须要有一个version相关的字段保存版本号）
    Integer version;
}
