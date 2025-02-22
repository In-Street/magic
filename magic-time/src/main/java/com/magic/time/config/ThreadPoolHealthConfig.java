package com.magic.time.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2025-02-22 19:16
 **/
@Component
public class ThreadPoolHealthConfig implements HealthIndicator {

    @Autowired
    @Qualifier("customOtherThreadPool")
    private ThreadPoolTaskExecutor poolTaskExecutor;

    @Override
    public Health health() {
        int active = poolTaskExecutor.getActiveCount();
        int max = poolTaskExecutor.getMaxPoolSize();
        int queueSize = poolTaskExecutor.getThreadPoolExecutor().getQueue().size();
        return Health.up()
                .withDetail("active_threads", active)
                .withDetail("max_threads", max)
                .withDetail("queue_size", queueSize)
                .build();
    }
}
