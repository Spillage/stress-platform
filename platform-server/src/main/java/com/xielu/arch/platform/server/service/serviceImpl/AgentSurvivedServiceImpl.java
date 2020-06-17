package com.xielu.arch.platform.server.service.serviceImpl;

import com.xielu.arch.platform.server.model.AgentSurvived;
import com.xielu.arch.platform.server.repo.AgentSurvivedRepo;
import com.xielu.arch.platform.server.service.AgentSurvivedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentSurvivedServiceImpl implements AgentSurvivedService {
    @Autowired
    AgentSurvivedRepo agentSurvivedRepo;

    @Override
    public void addHeartBreak(AgentSurvived agentSurvived) {
        agentSurvivedRepo.save(agentSurvived);
    }
    @Override
    public List<AgentSurvived> getLast1dActiveAgent(String date) {
        return agentSurvivedRepo.findAgentSurvivedsByCreateTimeIsLike(date);
    }

    @Override
    public AgentSurvived getLatestAgent() {
        return agentSurvivedRepo.getFirstByOrderByIdDesc();
    }
}
