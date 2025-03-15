package com.magic.time.service.business_development_100.spring_19;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 *  多个切面作用于同一连接点时，按优先级顺序执行：
 *    High Around  -  High Before  - Low Around  -  Low Before  -  目标方法执行  -  Low AfterReturning  -  Low After  -  Low  Around  -  High AfterReturning  -  High After  -  High  Around
 *
 * @author Cheng Yufei
 * @create 2022-05-15 18:56
 **/
@Aspect
@Component
@Slf4j
// 高优先级切面
@Order(1)
public class HighAopConfig {

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info(">> High Around before");
        Object proceed = pjp.proceed();
        log.info("<< High Around after");
        return proceed;
    }


    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void before() {
        log.info(">>High before");
    }

    @After("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void after() {
        log.info("<< High after");
    }

    @AfterReturning(returning = "result", pointcut = "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void afterReturn(Object result) {
        log.info(">>High  After Returning,{}", result);
    }

}
