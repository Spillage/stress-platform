package com.xielu.arch.platform.agent.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.xielu.arch.platform.agent.service.HealthReportService;
import com.xielu.arch.platform.agent.util.HttpUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;

public class HealthReportServiceImpl implements HealthReportService {

    private HttpUtil httpUtil = new HttpUtil();

    @Value("${server}")
    private String serverUrl;

    @Override
    @SneakyThrows
    public void reportHealthToServer(){
        JSONObject obj = new JSONObject();
        String timestamp = String.valueOf(System.currentTimeMillis());
        obj.put("ip",InetAddress.getLocalHost().getHostAddress());
        obj.put("timestamp",timestamp);
        String result = httpUtil.doPost(serverUrl,obj.toString());
    }
}
