package com.imooc.service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "app-store")
public class AppStoreConfig {
    private String privateKey;
    private String keyId;
    private String issuerId;
    private String bundleId;
    private boolean environment;  // true for production, false for sandbox
    private Long appAppleId;
    private List<String> rootCertificatePaths;  // 根证书文件路径列表
    
    @Autowired
    private ResourceLoader resourceLoader;
    
    private Set<InputStream> rootCertificates;
    
    @PostConstruct
    public void init() throws IOException {
        rootCertificates = new HashSet<>();
        for (String path : rootCertificatePaths) {
            Resource resource = resourceLoader.getResource(path);
            rootCertificates.add(resource.getInputStream());
        }
    }
    
    public Set<InputStream> getRootCertificates() {
        return rootCertificates;
    }
}
