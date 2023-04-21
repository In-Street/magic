package com.magic.interview.Config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Async异步线程池
 * @author Cheng Yufei
 * @create 2020-01-15 11:35
 **/
@Configuration
@Slf4j
public class ThreadPoolConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {

        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(30);
        poolTaskExecutor.setKeepAliveSeconds(30);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.setQueueCapacity(100);
        poolTaskExecutor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("task_demo_pool_%d")
                .setUncaughtExceptionHandler((thread,throwable)->{log.error(String.format(">>自定义线程池异常，thread:{}", thread), throwable);}).build());
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, objects) -> {
            log.error("异步执行异常", throwable);
            log.error("异步执行异常,method:{}",method.getName());
        };
    }
}
