package com.magic.time.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Cheng Yufei
 * @create 2025-02-22 18:45
 **/
@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncController {

    @Autowired
    private ApplicationContext applicationContext;



    @GetMapping("/a")
    public String async1 (){
        // applicationContext.getBean(AsyncController.class).asyncExceptionMethod();

        executor.execute(()->{exceptionMethod();});
        return "succ";
    }

    @Async
    public void asyncExceptionMethod() {
        log.info("Thread Name:{}", Thread.currentThread().getName());
        int a = 0;
        int i = 10 / a;
    }

    public void exceptionMethod() {
        log.info("Thread Name:{}", Thread.currentThread().getName());
        int a = 0;
        int i = 10 / a;
    }

    @Autowired
    @Qualifier("customOtherThreadPool")
    private ThreadPoolTaskExecutor executor;

    @GetMapping("/dynamicPool")
    public String  dynamicPool(int corePoolSize,int maxPoolSize){
        ThreadPoolExecutor threadPoolExecutor = executor.getThreadPoolExecutor();
        threadPoolExecutor.setCorePoolSize(corePoolSize);
        threadPoolExecutor.setMaximumPoolSize(maxPoolSize);
        return "success";
    }
}
