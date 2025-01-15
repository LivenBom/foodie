package com.imooc.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * App版本信息
 * @TableName app_version
 */
@TableName(value ="app_version")
@Data
public class AppVersion implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * App标识
     */
    private String appKey;

    /**
     * 版本号
     */
    private Integer versionCode;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 是否强制更新：0-否，1-是
     */
    private Integer forceUpdate;

    /**
     * 更新内容
     */
    private String updateContent;

    /**
     * 下载地址
     */
    private String downloadUrl;

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
