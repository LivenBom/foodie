CREATE TABLE app_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    app_key VARCHAR(50) NOT NULL COMMENT 'App唯一标识',
    app_name VARCHAR(100) NOT NULL COMMENT 'App名称',
    package_name VARCHAR(100) NOT NULL COMMENT '包名/Bundle ID',
    platform TINYINT NOT NULL COMMENT '平台：1-Android，2-iOS',
    description TEXT COMMENT 'App描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT 'App状态：0-禁用，1-启用',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_app_key (app_key),
    UNIQUE KEY uk_package_platform (package_name, platform)
) COMMENT='App基本信息表';
