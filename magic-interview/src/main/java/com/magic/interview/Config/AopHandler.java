package com.magic.interview.Config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * AOP
 */
@Aspect
@Component
@Slf4j
public class AopHandler {


    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping) ")

    public Object printMethodsExecutionTime(ProceedingJoinPoint pjp/*, RequestMapping requestMapping*/) throws Throwable {
        long start = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();

        log.info(">>> 开始请求: {},{}() with argument[s] = {}", requestURI, pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()));

        Object result = pjp.proceed();

        long usedTime = System.currentTimeMillis() - start;
        log.info("<<< 结束请求: {},{}(),耗时:{}ms with result = {}", requestURI, pjp.getSignature().getName(), usedTime, JSONObject.toJSONString(result));
        return result;
    }

}
