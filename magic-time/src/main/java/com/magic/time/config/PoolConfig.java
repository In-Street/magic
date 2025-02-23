package com.magic.time.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义 @Async 执行的线程池
 * @author Cheng Yufei
 * @create 2025-02-22 18:40
 **/
@Configuration
@Slf4j
public class PoolConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(30);
        poolTaskExecutor.setKeepAliveSeconds(30);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.setQueueCapacity(100);
        poolTaskExecutor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("async_task_demo_pool_%d")
                .setUncaughtExceptionHandler((thread,throwable)->{log.error(String.format(">>自定义线程池异常，thread:%s", thread), throwable);}).build());
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("异步执行异常", ex);
                log.error("异步执行异常,method:{}",method.getName());
            }
        };
    }

    @Bean(name={"customOtherThreadPool"})
    public ThreadPoolTaskExecutor getExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(6);
        poolTaskExecutor.setMaxPoolSize(31);
        poolTaskExecutor.setKeepAliveSeconds(31);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.setQueueCapacity(110);
        poolTaskExecutor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("other_task_demo_pool_%d")
                .setUncaughtExceptionHandler((thread,throwable)->{log.error(String.format(">>自定义线程池异常，thread:%s", thread), throwable);}).build());
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }
}
