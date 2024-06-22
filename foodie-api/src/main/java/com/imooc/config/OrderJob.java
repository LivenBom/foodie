package com.imooc.config;

import com.imooc.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    // cron表达式：https://cron.qqe2.com/
    /*
    * 使用定时任务关闭超期未支付订单，会存在弊端。
    * 1. 会有时间差，程序不严谨。
    * 2. 不支持集群。
    *    单击没毛病，使用集群后，就会有多个定时任务。
    *    解决方案：只使用一台计算机节点，单独用来执行定时任务。
    * 3. 会对数据库全表搜索，及其影响数据库性能。
    *    select * from order where created_time < ?;
    *
    * 定时任务：仅仅只适用于小型轻量级项目，传统项目。
    *
    * 解决方案：
    *   (1) 后续课程：消息队列，MQ -> RabbitMQ, RocketMQ, Kafka, ZeroMQ...
    *   (2) 延时任务（队列）
    * */
    @Scheduled(cron = "0 0 * * * *")
    public void autoCloseOrder() {
        // 每小时执行定时任务
        System.out.println("执行定时任务，当前时间为："
                + DateUtils.getCurrentDateString(DateUtils.DATETIME_PATTERN));
    }
}
