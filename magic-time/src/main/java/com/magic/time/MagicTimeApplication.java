package com.magic.time;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.time"}, exclude = {RabbitAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
@EnableAsync
public class MagicTimeApplication {



    public static void main(String[] args) {
        try {
        SpringApplication.run(MagicTimeApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Bean(name = "objectMapper")
    public ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //反序列化时有不存在的属性时忽略
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //序列化时忽略null 值的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

    @Bean(name={"otherThreadPool"})
    public ThreadPoolTaskExecutor getExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(6);
        poolTaskExecutor.setMaxPoolSize(31);
        poolTaskExecutor.setKeepAliveSeconds(31);
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.setQueueCapacity(110);
        // poolTaskExecutor.setTaskDecorator();
        poolTaskExecutor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("other_task_demo_pool_%d")
                .setUncaughtExceptionHandler((thread,throwable)->{log.error(String.format(">>线程池异常，thread:%s", thread), throwable);})
                .build());
        poolTaskExecutor.initialize();
        return poolTaskExecutor;
    }

}
