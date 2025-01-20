CREATE TABLE `iap_orders` (
  `id` varchar(64) NOT NULL COMMENT '订单主键',
  `user_id` varchar(64) NOT NULL COMMENT '用户id',
  `receiver_name` varchar(32) NOT NULL COMMENT '收货人姓名',
  `receiver_mobile` varchar(32) NOT NULL COMMENT '收货人手机号',
  `receiver_address` varchar(128) NOT NULL COMMENT '收货地址',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总价格',
  `real_pay_amount` decimal(10,2) NOT NULL COMMENT '实际支付总价格',
  `post_amount` decimal(10,2) NOT NULL COMMENT '邮费',
  `pay_method` int NOT NULL COMMENT '支付方式 1:微信 2:支付宝',
  `left_msg` varchar(128) DEFAULT NULL COMMENT '买家留言',
  `extand` varchar(32) DEFAULT NULL COMMENT '扩展字段',
  `is_comment` int NOT NULL COMMENT '买家是否评价 1：已评价，0：未评价',
  `is_delete` int NOT NULL DEFAULT '0' COMMENT '逻辑删除状态 1: 删除 0:未删除',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  `updated_time` datetime NOT NULL COMMENT '更新时间',
  `status` int NOT NULL COMMENT '订单状态 10:待付款 20:已付款，待发货 30:已发货，待收货 40:交易成功 50:交易关闭',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
