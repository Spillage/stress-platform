package com.xielu.arch.platform.server.service;

import com.xielu.arch.platform.server.model.AgentSurvived;

import java.util.List;

public interface AgentSurvivedService {
    void addHeartBreak(AgentSurvived agentSurvived);
    List<AgentSurvived> getLast1dActiveAgent(String date);
    AgentSurvived getLatestAgent();
}
