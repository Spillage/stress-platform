package com.xielu.arch.platform.agent.service;

import lombok.SneakyThrows;

public interface HealthReportService {
    @SneakyThrows
    void reportHealthToServer();
}
