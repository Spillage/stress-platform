package com.xielu.arch.platform.agent.controller;

import com.xielu.arch.platform.agent.service.HealthReportService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class HeathCheckController {

    @Autowired
    HealthReportService healthReportService;

    /**
     * 起一个线程来上报心跳
     */
    @Async
    @SneakyThrows
    @Scheduled(cron = "0 */5 * * * ?")
    public void reportHealth(){
        Runnable runnable = () -> { healthReportService.reportHealthToServer(); };
        Thread temp = new Thread(runnable);
        temp.start();
    }
}
