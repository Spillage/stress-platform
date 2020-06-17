package com.xielu.arch.platform.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.xielu.arch.platform.server.model.AgentSurvived;
import com.xielu.arch.platform.server.service.AgentSurvivedService;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HealthCheckController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class);

    @Autowired
    AgentSurvivedService agentSurvivedService;

    /**
     * 接收心跳
     * @param obj
     */
    @PostMapping
    @SneakyThrows
    public void getAgentHealthCheck(@RequestBody(required = false) JSONObject obj){
        LOGGER.info("Receive heartbreak..." + obj);
        AgentSurvived agentSurvived = new AgentSurvived();
        agentSurvived.setAgentIP(obj.getString("ip"));
        agentSurvived.setCreateTime(obj.getString("timestamp"));
        LOGGER.info("Agent heartbreak: ",agentSurvived);
        agentSurvivedService.addHeartBreak(agentSurvived);
    }

    @GetMapping("/showAgent")
    @SneakyThrows
    public List<AgentSurvived> showActiveAgent(@RequestParam(value = "date") String date){
        String temp = "%" + date + "%";
        List<AgentSurvived> result = new ArrayList<>();
        result = agentSurvivedService.getLast1dActiveAgent(temp);
        return result;
    }



}
