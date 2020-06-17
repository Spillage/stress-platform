package com.xielu.arch.platform.server.repo;

import com.xielu.arch.platform.server.model.AgentSurvived;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AgentSurvivedRepo extends CrudRepository<AgentSurvived, Long> {
    List<AgentSurvived> findAgentSurvivedsByCreateTimeIsLike(String date);
    AgentSurvived getFirstByOrderByIdDesc();
}
