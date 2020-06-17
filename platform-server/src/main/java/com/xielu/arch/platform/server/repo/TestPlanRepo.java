package com.xielu.arch.platform.server.repo;

import com.xielu.arch.platform.server.model.TestPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface TestPlanRepo extends CrudRepository<TestPlan, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TestPlan testPlan set testPlan.status = ?2 where testPlan.id = ?1")
    public void updateByIdWithRunning(@Param(value = "testPlanId") Long testPlanId, @Param(value = "status") String status);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update TestPlan testPlan set testPlan.status = :status,testPlan.report = :report where testPlan.id = :testPlanId")
    public void updateByTestPlanIdAndReport(@Param(value = "testPlanId") Long testPlanId, @Param(value = "status") String status, @Param(value = "report") String report);

    public TestPlan getByIdOrderByIdDesc(Long limit);

    public TestPlan getFirstById(Long testPlanId);

    public List<TestPlan> findAll();

}
