package com.xielu.arch.platform.agent.controller;

import com.xielu.arch.platform.agent.service.ExecutionService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskGainController {

    //@RequestParam(value = "commitId", required = true) String commitId, String threadNum, String connNum, String elasped, String url
    @Autowired
    private ExecutionService executionService;

    @PostMapping("/exec")
    @ResponseBody
    @SneakyThrows
    public String TaskGain() {
        executionService.TestExecute("", 0, 0, 0, "");
        return "";
    }


}
