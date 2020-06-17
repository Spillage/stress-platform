package com.xielu.arch.platform.agent.service.serviceImpl;

import com.xielu.arch.platform.agent.service.ExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionServiceImpl.class);

    @Value("${wrk}")
    private String wrkPath;

    @Async
    @Override
    public void TestExecute(String fileName, int threadNum, int connNum, int elapsed, String url) {
        ProcessBuilder builder = new ProcessBuilder();

        builder.command(wrkPath, "-c"+connNum, "-t"+threadNum, "-d"+elapsed+"s", "-s"+" "+fileName,"--latency", url);
        builder.directory(new File(System.getProperty("user.home")));
        try {
            Process process = builder.start();
            int exitCode = process.waitFor();
            process.getOutputStream();
            LOGGER.info("exitCode "+exitCode);
        } catch (Exception e) {
            LOGGER.error(e.getCause().getMessage());
        }


    }
}

