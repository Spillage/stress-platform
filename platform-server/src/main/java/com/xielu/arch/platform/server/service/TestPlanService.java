package com.xielu.arch.platform.server.service;

import com.xielu.arch.platform.server.model.TestPlan;

import java.util.List;


public interface TestPlanService {
    void addTestPlan(TestPlan testPlan);
    void updateByIdWithRunning(Long testPlanId,String status);
    void updateByTestPlanIdAndReport(Long testPlanId,String status,String report);
    TestPlan getLatestTestPlan();
    TestPlan getById(Long testPlanId);
    List<TestPlan> getAllPlans();
}
