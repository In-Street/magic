package com.magic.time.service.business_development_100.spring_19;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *  单个切面的执行顺序：
 * @Around  -  @Before  -  目标方法执行  -  @AfterReturning / @AfterThrowing   -  @After  -  @Around
 *
 *
 * @author Cheng Yufei
 * @create 2022-05-15 18:56
 **/
@Aspect
@Component
@Slf4j
// 低优先级切面
@Order(2)
public class AopConfig {

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info(">>Around before");
        Object proceed = pjp.proceed();
        log.info("<< Around after");
        return proceed;
    }


    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void before() {
        log.info(">> before");
    }

    @After("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void after() {
        log.info("<< after");
    }

    @AfterReturning(returning = "result", pointcut = "@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void afterReturn(Object result) {
        log.info(">> After Returning,{}", result);
    }

}
