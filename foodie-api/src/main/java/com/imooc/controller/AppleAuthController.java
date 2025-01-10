package com.imooc.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.AppleUserBO;
import com.imooc.pojo.dto.AppleLoginRequest;
import com.imooc.service.AppleAuthService;
import com.imooc.service.UsersService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.JwtUtils;
import com.imooc.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth/apple")
public class AppleAuthController {

    @Autowired
    private AppleAuthService appleAuthService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JwtUtils jwtUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/callback")
    public IMOOCJSONResult handleAppleCallback(
            @RequestParam("code") String authCode,
            @RequestParam("identityToken") String identityToken,
            @RequestParam(value = "user", required = false) String userInfo) {
        
        try {
            // 验证 Apple 返回的授权码和身份令牌
            Map<String, Object> appleUserInfo = appleAuthService.validateAuthCode(authCode, identityToken);
            if (appleUserInfo == null) {
                return IMOOCJSONResult.errorMsg("Apple authentication failed");
            }

            String appleUserId = (String) appleUserInfo.get("sub");
            
            // 检查用户是否已存在
            Users user = usersService.queryUserByAppleId(appleUserId);
            
            if (user == null) {
                // 创建新用户
                user = new Users();
                user.setAppleId(appleUserId);
                // 解析并设置用户信息
                if (userInfo != null) {
                    try {
                        JsonNode userNode = objectMapper.readTree(userInfo);
                        JsonNode nameNode = userNode.path("name");
                        
                        String givenName = nameNode.path("firstName").asText(null);
                        String familyName = nameNode.path("lastName").asText(null);
                        String email = userNode.path("email").asText(null);
                        
                        // 设置用户名（使用 email 或生成一个随机用户名）
                        String username = email != null ? email.split("@")[0] : "user_" + appleUserId.substring(0, 8);
                        user.setUsername(username);
                        
                        // 设置昵称（如果有名字，使用名字；否则使用用户名）
                        if (givenName != null || familyName != null) {
                            String nickname = (givenName != null ? givenName : "") +
                                           (familyName != null ? " " + familyName : "");
                            user.setNickname(nickname.trim());
                        } else {
                            user.setNickname(username);
                        }
                        
                        // 设置邮箱
                        if (email != null) {
                            user.setEmail(email);
                        }
                        
                        log.info("Created new user from Apple login: username={}, nickname={}, email={}", 
                                username, user.getNickname(), email);
                    } catch (Exception e) {
                        log.error("Error parsing Apple user info: {}", e.getMessage());
                    }
                }
                // 为Apple登录用户设置默认密码（使用他们的Apple ID的MD5值）
                try {
                    String defaultPassword = MD5Utils.getMD5Str(appleUserId);
                    user.setPassword(defaultPassword);
                } catch (Exception e) {
                    log.error("Failed to generate MD5 password", e);
                    return IMOOCJSONResult.errorMsg("Failed to create user");
                }
                
                user = usersService.createUser(user);
            }

            // 生成 token
            try {
                String accessToken = jwtUtils.generateAccessToken(user.getId());
                String refreshToken = jwtUtils.generateRefreshToken(user.getId());

                // 构造返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("user", convertToAppleUserBO(user));
                result.put("token", accessToken);
                result.put("refreshToken", refreshToken);

                return IMOOCJSONResult.ok(result);

            } catch (Exception e) {
                log.error("Error generating tokens: {}", e.getMessage(), e);
                return IMOOCJSONResult.errorMsg("Failed to generate authentication tokens");
            }
        } catch (Exception e) {
            log.error("Apple authentication failed: {}", e.getMessage());
            return IMOOCJSONResult.errorMsg("Authentication failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody AppleLoginRequest loginRequest) {
        log.info("Received Apple login request");
        log.info("Identity Token: {}", loginRequest.getIdentityToken());
        
        try {
            // 如果没有identityToken，返回错误信息
            if (org.apache.commons.lang3.StringUtils.isBlank(loginRequest.getIdentityToken())) {
                return IMOOCJSONResult.errorMsg("Identity token is required");
            }

            // 验证 Apple 返回的 Identity Token
            Map<String, Object> appleUserInfo = appleAuthService.validateIdentityToken(loginRequest.getIdentityToken());
            if (appleUserInfo == null) {
                log.error("Apple authentication failed: invalid identity token");
                return IMOOCJSONResult.errorMsg("Apple authentication failed: invalid identity token");
            }

            // 获取用户信息
            String appleUserId = (String) appleUserInfo.get("sub");
            if (org.apache.commons.lang3.StringUtils.isBlank(appleUserId)) {
                log.error("Apple authentication failed: no user ID in token");
                return IMOOCJSONResult.errorMsg("Apple authentication failed: invalid user info");
            }

            // 查找或创建用户
            Users user = usersService.queryUserByAppleId(appleUserId);
            if (user == null) {
                log.info("Creating new user for Apple ID: {}", appleUserId);
                // 创建新用户
                user = new Users();
                user.setAppleId(appleUserId);
                
                // 设置用户信息
                String email = (String) appleUserInfo.get("email");
                if (email != null) {
                    user.setEmail(email);
                    user.setUsername(email.split("@")[0]);
                    user.setNickname(user.getUsername());
                } else {
                    // 如果没有邮箱，使用 apple user id 的一部分作为用户名
                    user.setUsername("user_" + appleUserId.substring(0, 8));
                    user.setNickname(user.getUsername());
                }

                // 为Apple登录用户设置默认密码
                try {
                    String defaultPassword = MD5Utils.getMD5Str(appleUserId);
                    user.setPassword(defaultPassword);
                } catch (Exception e) {
                    log.error("Failed to generate MD5 password: {}", e.getMessage(), e);
                    return IMOOCJSONResult.errorMsg("Failed to create user: password generation error");
                }
                
                try {
                    user = usersService.createUser(user);
                    if (user == null) {
                        log.error("Failed to create user in database");
                        return IMOOCJSONResult.errorMsg("Failed to create user in database");
                    }
                } catch (Exception e) {
                    log.error("Error creating user: {}", e.getMessage(), e);
                    return IMOOCJSONResult.errorMsg("Failed to create user: " + e.getMessage());
                }
            }

            // 生成token
            try {
                String accessToken = jwtUtils.generateAccessToken(user.getId());
                String refreshToken = jwtUtils.generateRefreshToken(user.getId());

                // 构造返回结果
                Map<String, Object> result = new HashMap<>();
                result.put("user", convertToAppleUserBO(user));
                result.put("token", accessToken);
                result.put("refreshToken", refreshToken);

                return IMOOCJSONResult.ok(result);
            } catch (Exception e) {
                log.error("Error generating tokens: {}", e.getMessage(), e);
                return IMOOCJSONResult.errorMsg("Failed to generate authentication tokens");
            }
        } catch (Exception e) {
            log.error("Unexpected error during Apple login: {}", e.getMessage(), e);
            return IMOOCJSONResult.errorMsg("Login failed: " + e.getMessage());
        }
    }

    private AppleUserBO convertToAppleUserBO(Users user) {
        if (user == null) {
            return null;
        }
        AppleUserBO userBO = new AppleUserBO();
        userBO.setId(user.getId());
        userBO.setUsername(user.getUsername());
        userBO.setAppleId(user.getAppleId());
        userBO.setEmail(user.getEmail());
        userBO.setSex(user.getSex());
        userBO.setFace(user.getFace());
        return userBO;
    }
}
