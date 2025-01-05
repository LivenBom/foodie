package com.imooc.service;

import com.imooc.config.AppleAuthConfig;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Apple Auth Service
 */
@Service
public class AppleAuthService {
    
    private static final Logger log = LoggerFactory.getLogger(AppleAuthService.class);

    @Autowired
    private AppleAuthConfig appleAuthConfig;
    
    @Autowired
    private RestTemplate restTemplate;

    private static final String APPLE_AUTH_URL = "https://appleid.apple.com/auth/token";
    private static final String APPLE_KEYS_URL = "https://appleid.apple.com/auth/keys";

    public String createClientSecret() throws Exception {
        Date now = new Date();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(appleAuthConfig.getTeamId())
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + 15777000000L)) // 6 months
                .audience("https://appleid.apple.com")
                .subject(appleAuthConfig.getClientId())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.ES256)
                        .keyID(appleAuthConfig.getKeyId())
                        .build(),
                claims);

        signedJWT.sign(new ECDSASigner(getPrivateKey()));
        return signedJWT.serialize();
    }

    private ECPrivateKey getPrivateKey() throws Exception {
        String privateKey = appleAuthConfig.getPrivateKey()
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    private Map<String, Object> validateIdentityTokenInternal(String identityToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            
            // 验证令牌是否过期
            Date expirationTime = claims.getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                log.error("Identity token has expired");
                return null;
            }

            // 验证令牌的audience是否匹配
            String audience = claims.getAudience().get(0);
            if (!appleAuthConfig.getClientId().equals(audience)) {
                log.error("Identity token has invalid audience");
                return null;
            }

            // 从claims中提取用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("sub", claims.getSubject()); // Apple用户ID
            userInfo.put("email", claims.getClaim("email"));
            userInfo.put("email_verified", claims.getClaim("email_verified"));
            
            return userInfo;
            
        } catch (ParseException e) {
            log.error("Failed to parse identity token", e);
            return null;
        }
    }

    public Map<String, Object> validateAuthCode(String authCode, String identityToken) {
        // 验证identityToken
        return validateIdentityTokenInternal(identityToken);
    }

    public Map<String, Object> validateIdentityToken(String identityToken) {
        // 直接调用内部验证方法
        return validateIdentityTokenInternal(identityToken);
    }

    /**
     * 验证 Apple Identity Token
     * @param identityToken Apple 返回的 identity token
     * @return 用户信息
     */
    public Map<String, Object> validateIdentityToken2(String identityToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(identityToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            
            // 验证发行者
            if (!"https://appleid.apple.com".equals(claimsSet.getIssuer())) {
                return null;
            }
            
            // 验证受众（client_id）
            if (!appleAuthConfig.getClientId().equals(claimsSet.getAudience().get(0))) {
                return null;
            }
            
            // 验证过期时间
            if (new Date().after(claimsSet.getExpirationTime())) {
                return null;
            }
            
            // 返回用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("sub", claimsSet.getSubject());
            userInfo.put("email", claimsSet.getClaim("email"));
            userInfo.put("email_verified", claimsSet.getClaim("email_verified"));
            userInfo.put("is_private_email", claimsSet.getClaim("is_private_email"));
            
            return userInfo;
        } catch (ParseException e) {
            log.error("Error parsing Identity Token: {}", e.getMessage());
            return null;
        }
    }
}
