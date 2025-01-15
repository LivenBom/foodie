package com.imooc.controller;

import com.imooc.pojo.AppInfo;
import com.imooc.pojo.AppVersion;
import com.imooc.pojo.vo.AppVersionVO;
import com.imooc.service.AppVersionService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "App版本相关接口")
@RestController
@RequestMapping("app")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @Operation(summary = "检查App更新")
    @GetMapping("/check-update")
    public IMOOCJSONResult checkUpdate(
            @Parameter(description = "App标识", required = true)
            @RequestParam String appKey,
            @Parameter(description = "当前版本号", required = true)
            @RequestParam Integer versionCode) {
        
        AppVersionVO versionInfo = appVersionService.checkUpdate(appKey, versionCode);
        return IMOOCJSONResult.ok(versionInfo);
    }

    @Operation(summary = "获取App列表")
    @GetMapping("/list")
    public IMOOCJSONResult getAppList() {
        List<AppInfo> appList = appVersionService.getAppList();
        return IMOOCJSONResult.ok(appList);
    }

    @Operation(summary = "创建App")
    @PostMapping("/create")
    public IMOOCJSONResult createApp(@RequestBody AppInfo appInfo) {
        appVersionService.createApp(appInfo);
        return IMOOCJSONResult.ok();
    }

    @Operation(summary = "更新App")
    @PostMapping("/update")
    public IMOOCJSONResult updateApp(@RequestBody AppInfo appInfo) {
        appVersionService.updateApp(appInfo);
        return IMOOCJSONResult.ok();
    }

    @Operation(summary = "获取版本列表")
    @GetMapping("/version/list")
    public IMOOCJSONResult getVersionList(
            @Parameter(description = "App标识", required = true)
            @RequestParam String appKey) {
        List<AppVersion> versionList = appVersionService.getVersionList(appKey);
        return IMOOCJSONResult.ok(versionList);
    }

    @Operation(summary = "创建新版本")
    @PostMapping("/version/create")
    public IMOOCJSONResult createVersion(@RequestBody AppVersion version) {
        appVersionService.createVersion(version);
        return IMOOCJSONResult.ok();
    }
}
