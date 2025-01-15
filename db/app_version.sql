CREATE TABLE app_version (
    id INT AUTO_INCREMENT PRIMARY KEY,
    app_key VARCHAR(50) NOT NULL COMMENT 'App标识，用于区分不同的App',
    version_code INT NOT NULL COMMENT '版本号，用于比较版本大小',
    version_name VARCHAR(20) NOT NULL COMMENT '版本名称，如1.0.0',
    force_update TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否强制更新：0-否，1-是',
    update_content TEXT NOT NULL COMMENT '更新内容',
    download_url VARCHAR(255) NOT NULL COMMENT '下载地址',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_app_key (app_key)
) COMMENT='App版本信息表';
