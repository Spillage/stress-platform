package com.xielu.arch.platform.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.xielu.arch.platform.server.model.AgentSurvived;
import com.xielu.arch.platform.server.model.TestPlan;
import com.xielu.arch.platform.server.service.AgentSurvivedService;
import com.xielu.arch.platform.server.service.TestPlanService;
import com.xielu.arch.platform.server.util.HttpUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestScriptController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestScriptController.class);

    @Autowired
    TestPlanService testPlanService;

    @Autowired
    AgentSurvivedService agentSurvivedService;

    /**
     * 增加测试计划
     *
     * @param params
     * @return
     */
    @CrossOrigin
    @PostMapping("/addtestplan")
    @SneakyThrows
    public void getTestParamsAndCreatePlan(@RequestBody JSONObject params) {
        LOGGER.info("Receive new plan " + params);
        JSONObject json = params.getJSONObject("script");


        TestPlan testPlan = new TestPlan();
        testPlan.setConnNum(params.getIntValue("connNum"));
        testPlan.setThreadNum(testPlan.getConnNum() / 4 > 1 ? testPlan.getConnNum() / 4 : 1);
        testPlan.setElaspe(params.getIntValue("elapse"));
        testPlan.setCreator("xielu");
        testPlan.setHost(params.getString("host"));
        testPlan.setStatus("None");
        testPlan.setReport("None");
        testPlan.setWrkScript(json.toString());
        testPlanService.addTestPlan(testPlan);
    }

    /**
     * 将测试脚本发送到agent
     *
     * @param params
     */
    @CrossOrigin
    @PostMapping("/sendtestplan")
    @SneakyThrows
    public void sendTestPlanToAgent(@RequestBody JSONObject params) {
        LOGGER.info("Send testplan to agent..." + params.getString("agent") + params);

        HttpUtil httpUtil = new HttpUtil();
        httpUtil.doPost("http://" + params.getString("agent") + "/input", params.toString());
        testPlanService.updateByIdWithRunning(params.getLong("testPlanId"), "running");
    }

    /**
     * 用于agent上传测试报告
     *
     * @param params
     */
    @CrossOrigin
    @RequestMapping(value = "/reportupload")
    @SneakyThrows
    public void updateTestPlanFromAgent(@RequestBody JSONObject params) {
        LOGGER.info("Receive testplan from agent..." + params);
        if (StringUtils.isEmpty(params.getString("report"))) {
            testPlanService.updateByTestPlanIdAndReport(params.getLong("testPlanId"), "failure", params.getString("report"));
        } else {
            testPlanService.updateByTestPlanIdAndReport(params.getLong("testPlanId"), "success", params.getString("report"));
        }
    }

    /**
     * 获取最新测试计划
     * 用来展示
     *
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/latestplan")
    @SneakyThrows
    public JSONObject getLatestPlan() {
        TestPlan temp = testPlanService.getLatestTestPlan();
        JSONObject json = new JSONObject();
        JSONObject script = new JSONObject();
        //此处存疑，应该怎么操作呢？
        //数据库读出来是{path=/, method=GET}
        script.put("path", temp.getWrkScript());
        //script.put("methoc",temp.)

        json.put("agent", agentSurvivedService.getLatestAgent().getAgentIP());
        json.put("testPlanId", temp.getId());
        json.put("elapse", temp.getElaspe());
        json.put("connNum", temp.getConnNum());
        json.put("threadNum", temp.getThreadNum());
        json.put("creator", temp.getCreator());
        json.put("script", script);
        return json;
    }

    /**
     * 返回html版测试报告
     *
     * @param testPlanId
     * @return
     */
    @RequestMapping(value = "/getreport", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    @SneakyThrows
    public String getTestReport(@RequestParam("testPlanId") String testPlanId) {
        LOGGER.info("Get report of testPlan " + testPlanId);
        Long id = Long.parseLong(testPlanId);
        TestPlan temp = testPlanService.getById(id);
        if (temp == null) {
            return "";
        } else {
            return temp.getReport();
        }
    }

    /**
     * 获取所有的测试计划
     *
     * @return
     */
    @CrossOrigin
    @GetMapping(value = "/showall")
    @SneakyThrows
    public List<TestPlan> getAllTestPlan() {
        List<TestPlan> list = new ArrayList<>();
        list = testPlanService.getAllPlans();
        return list;
    }

    /**
     * 获取最新的agent
     *
     * @return
     */
    @CrossOrigin("*")
    @GetMapping(value = "/getagent")
    @SneakyThrows
    public AgentSurvived getLatestAgent() {
        return agentSurvivedService.getLatestAgent();
    }
}
