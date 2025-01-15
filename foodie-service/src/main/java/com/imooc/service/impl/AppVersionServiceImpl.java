package com.imooc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.imooc.mapper.AppInfoMapper;
import com.imooc.mapper.AppVersionMapper;
import com.imooc.pojo.AppInfo;
import com.imooc.pojo.AppVersion;
import com.imooc.pojo.vo.AppVersionVO;
import com.imooc.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Override
    public AppVersionVO checkUpdate(String appKey, Integer currentVersionCode) {
        // 先检查App是否存在且启用
        LambdaQueryWrapper<AppInfo> appInfoWrapper = new LambdaQueryWrapper<>();
        appInfoWrapper.eq(AppInfo::getAppKey, appKey)
                     .eq(AppInfo::getStatus, 1);
        AppInfo appInfo = appInfoMapper.selectOne(appInfoWrapper);
        
        // 如果App不存在或已禁用，返回无更新
        if (appInfo == null) {
            AppVersionVO vo = new AppVersionVO();
            vo.setHasNewVersion(false);
            vo.setForceUpdate(false);
            return vo;
        }

        // 查询最新版本信息
        LambdaQueryWrapper<AppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppVersion::getAppKey, appKey)
                   .orderByDesc(AppVersion::getVersionCode)
                   .last("LIMIT 1");
        
        AppVersion latestVersion = appVersionMapper.selectOne(queryWrapper);
        
        AppVersionVO vo = new AppVersionVO();
        if (latestVersion == null || latestVersion.getVersionCode() <= currentVersionCode) {
            // 没有新版本
            vo.setHasNewVersion(false);
            vo.setForceUpdate(false);
            return vo;
        }
        
        // 有新版本
        vo.setHasNewVersion(true);
        vo.setForceUpdate(latestVersion.getForceUpdate() == 1);
        vo.setVersionCode(latestVersion.getVersionCode());
        vo.setVersionName(latestVersion.getVersionName());
        vo.setUpdateContent(latestVersion.getUpdateContent());
        vo.setDownloadUrl(latestVersion.getDownloadUrl());
        
        return vo;
    }

    @Override
    public List<AppInfo> getAppList() {
        LambdaQueryWrapper<AppInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AppInfo::getCreatedTime);
        return appInfoMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createApp(AppInfo appInfo) {
        // 检查App标识是否已存在
        LambdaQueryWrapper<AppInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppInfo::getAppKey, appInfo.getAppKey());
        if (appInfoMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("App标识已存在");
        }

        // 检查包名和平台组合是否已存在
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppInfo::getPackageName, appInfo.getPackageName())
                   .eq(AppInfo::getPlatform, appInfo.getPlatform());
        if (appInfoMapper.selectCount(queryWrapper) > 0) {
            throw new RuntimeException("该平台下的包名已存在");
        }

        appInfoMapper.insert(appInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateApp(AppInfo appInfo) {
        // 检查App是否存在
        AppInfo existingApp = appInfoMapper.selectById(appInfo.getId());
        if (existingApp == null) {
            throw new RuntimeException("App不存在");
        }

        // 如果修改了包名，检查新包名是否与其他App冲突
        if (!existingApp.getPackageName().equals(appInfo.getPackageName())) {
            LambdaQueryWrapper<AppInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AppInfo::getPackageName, appInfo.getPackageName())
                       .eq(AppInfo::getPlatform, appInfo.getPlatform())
                       .ne(AppInfo::getId, appInfo.getId());
            if (appInfoMapper.selectCount(queryWrapper) > 0) {
                throw new RuntimeException("该平台下的包名已存在");
            }
        }

        appInfoMapper.updateById(appInfo);
    }

    @Override
    public List<AppVersion> getVersionList(String appKey) {
        LambdaQueryWrapper<AppVersion> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppVersion::getAppKey, appKey)
                   .orderByDesc(AppVersion::getVersionCode);
        return appVersionMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createVersion(AppVersion version) {
        // 检查App是否存在且启用
        LambdaQueryWrapper<AppInfo> appInfoWrapper = new LambdaQueryWrapper<>();
        appInfoWrapper.eq(AppInfo::getAppKey, version.getAppKey())
                     .eq(AppInfo::getStatus, 1);
        if (appInfoMapper.selectCount(appInfoWrapper) == 0) {
            throw new RuntimeException("App不存在或已禁用");
        }

        // 检查版本号是否已存在
        LambdaQueryWrapper<AppVersion> versionWrapper = new LambdaQueryWrapper<>();
        versionWrapper.eq(AppVersion::getAppKey, version.getAppKey())
                     .eq(AppVersion::getVersionCode, version.getVersionCode());
        if (appVersionMapper.selectCount(versionWrapper) > 0) {
            throw new RuntimeException("该版本号已存在");
        }

        appVersionMapper.insert(version);
    }
}
