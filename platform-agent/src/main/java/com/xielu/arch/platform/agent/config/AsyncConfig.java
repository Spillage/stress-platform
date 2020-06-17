package com.xielu.arch.platform.agent.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncConfig.class);

    private int queueSize = 1024;

    private int coreSize = 2;

    private int maxSize = 50;

    private int keepAliveTime = 30;

    private String namePrefix = "agent-worker-";

    private BlockingQueue<Runnable> queue;

    @Override
    public Executor getAsyncExecutor() {
        if (coreSize <= 0) {
            coreSize = Runtime.getRuntime().availableProcessors();
        }

        queue = new LinkedBlockingQueue<>(queueSize);
        return new ThreadPoolExecutor(
                coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS, queue,
                new ThreadFactory() {
                    private AtomicInteger seq = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r);
                        t.setName(namePrefix + seq.getAndAdd(1));
                        return t;
                    }
                },
                new ThreadPoolExecutor.DiscardPolicy()
        );
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                LOGGER.error("async error, method: " + method.getName(), ex);
            }
        };
    }

    @PreDestroy
    public void preDestroy() {
        while (queue.peek() != null) {
            LOGGER.info("default executor queue size: " + queue.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("", e);
            }
        }

        LOGGER.info("default executor queue is clear!");
    }
}
