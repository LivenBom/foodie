package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class AppVersionVO {
    /**
     * 是否有新版本
     */
    private Boolean hasNewVersion;

    /**
     * 是否强制更新
     */
    private Boolean forceUpdate;

    /**
     * 最新版本号
     */
    private Integer versionCode;

    /**
     * 最新版本名称
     */
    private String versionName;

    /**
     * 更新内容
     */
    private String updateContent;

    /**
     * 下载地址
     */
    private String downloadUrl;
}
