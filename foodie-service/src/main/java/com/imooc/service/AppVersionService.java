package com.imooc.service;

import com.imooc.pojo.AppInfo;
import com.imooc.pojo.AppVersion;
import com.imooc.pojo.vo.AppVersionVO;

import java.util.List;

public interface AppVersionService {
    /**
     * 检查App更新
     * @param appKey App标识
     * @param currentVersionCode 当前版本号
     * @return 更新信息
     */
    AppVersionVO checkUpdate(String appKey, Integer currentVersionCode);

    /**
     * 获取App列表
     * @return App列表
     */
    List<AppInfo> getAppList();

    /**
     * 创建App
     * @param appInfo App信息
     */
    void createApp(AppInfo appInfo);

    /**
     * 更新App
     * @param appInfo App信息
     */
    void updateApp(AppInfo appInfo);

    /**
     * 获取版本列表
     * @param appKey App标识
     * @return 版本列表
     */
    List<AppVersion> getVersionList(String appKey);

    /**
     * 创建新版本
     * @param version 版本信息
     */
    void createVersion(AppVersion version);
}
