package com.imooc.config;

import com.imooc.utils.DateUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    // cron表达式：https://cron.qqe2.com/
    @Scheduled(cron = "0/3 * * * * ?")
    public void autoCloseOrder() {
        // 每隔三秒执行定时任务
        System.out.println("执行定时任务，当前时间为："
                + DateUtils.getCurrentDateString(DateUtils.DATETIME_PATTERN));
    }
}
