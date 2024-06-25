package com.imooc.controller.center;

import com.imooc.controller.BaseController;
import com.imooc.pojo.Users;
import com.imooc.resource.FileUpload;
import com.imooc.service.center.CenterUsersService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.DateUtils;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JsonUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Tags({@Tag(name = "用户中心")})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUsersService centerUsersService;

    @Autowired
    private FileUpload fileUpload;

    @GetMapping("/userInfo")
    public IMOOCJSONResult userInfo(@RequestParam String userId) {
        if(StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        Users user = centerUsersService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(user);
    }


    @PostMapping("/uploadFace")
    public IMOOCJSONResult uploadFace(@RequestParam String userId,
                                      MultipartFile file,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        // 头像保存地址
        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userid, 用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;
        // 开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                // 获取文件上传的文件名称
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNoneBlank(fileName)) {
                    // 期望格式：face-{userid}.png
                    String[] fileNameArr = fileName.split("\\.");
                    // 获取文件后缀名
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    // 这里做文件格式的校验，主要是为了避免黑客上传一些脚本文件，然后入侵我们的服务器
                    if (!suffix.equalsIgnoreCase("png") &&
                        !suffix.equalsIgnoreCase("jpg") &&
                        !suffix.equalsIgnoreCase("jpeg")) {
                        return IMOOCJSONResult.errorMsg("图片格式不正确");
                    }

                    // 文件名称重组, 覆盖式上传(如果是要增量式：额外拼接当前时间）
                    String newFileName = "face-" + userId + "." + suffix;

                    // 上传的头像最终保存的位置
                    String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    // 用于提供给web服务访问的地址
                    uploadPathPrefix += ("/" + newFileName);

                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null) {
                        // 需确保父目录存在，才能创建文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    // 文件输出保存到目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            return IMOOCJSONResult.errorMsg("文件不能为空");
        }

        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();
        // 由于浏览器可能存在缓存，所以需要加上时间戳来保证更新后的图片能够及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix + "?t=" + DateUtils.getCurrentDateString(DateUtils.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUsersService.updateUserFace(userId, finalUserFaceUrl);

        // 同步到前端
        userResult = setNullProperty(userResult);
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }

}
