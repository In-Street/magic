package com.magic.time.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义 @Scheduled 执行的线程池
 * @author Cheng Yufei
 * @create 2025-02-23 18:35
 **/
@Configuration
public class ScheduledPoolConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(threadPoolTaskScheduler());
    }

    @Bean(name = "scheduleTaskExecutor")
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(10);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("schedule-pool-");

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        executor.initialize();
        System.out.println(">>pool size: " + executor.getPoolSize());
        System.out.println(">>pool size: " + executor.getScheduledThreadPoolExecutor().getPoolSize());
        System.out.println(">>core pool size: " + executor.getScheduledThreadPoolExecutor().getCorePoolSize());
        return executor;
    }
}
