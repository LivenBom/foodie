package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * App基本信息
 * @TableName app_info
 */
@TableName(value ="app_info")
@Data
public class AppInfo implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * App唯一标识
     */
    private String appKey;

    /**
     * App名称
     */
    private String appName;

    /**
     * 包名/Bundle ID
     */
    private String packageName;

    /**
     * 平台：1-Android，2-iOS
     */
    private Integer platform;

    /**
     * App描述
     */
    private String description;

    /**
     * App状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
