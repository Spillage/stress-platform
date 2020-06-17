package com.xielu.arch.platform.server.service.serviceImpl;

import com.xielu.arch.platform.server.model.TestPlan;
import com.xielu.arch.platform.server.repo.TestPlanRepo;
import com.xielu.arch.platform.server.service.TestPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestPlanServiceImpl implements TestPlanService {
    @Autowired
    TestPlanRepo testPlanRepo;

    @Override
    public void addTestPlan(TestPlan testPlan) {
        testPlanRepo.save(testPlan);
    }

    @Override
    public void updateByIdWithRunning(Long testPlanId, String status) {
        testPlanRepo.updateByIdWithRunning(testPlanId, status);
    }

    /**
     * @param testPlanId
     * @param status
     * @param report
     */
    @Override
    public void updateByTestPlanIdAndReport(Long testPlanId, String status, String report) {
        testPlanRepo.updateByTestPlanIdAndReport(testPlanId,status,report);
    }

    @Override
    public TestPlan getLatestTestPlan() {
        return testPlanRepo.getByIdOrderByIdDesc(1L);
    }

    @Override
    public TestPlan getById(Long testPlanId) {
        return testPlanRepo.getFirstById(testPlanId);
    }

    @Override
    public List<TestPlan> getAllPlans() {
        return testPlanRepo.findAll();
    }


}
