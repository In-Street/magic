package com.magic.time.service.business_development_100.reflection_18;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author Cheng Yufei
 * @create 2022-04-03 17:56
 **/
@Slf4j
public class ReflectionService {

    public  void age(int age) {
        log.info(" int age:{}", age);
    }

    public  void age(Integer age) {
        log.info("integer age:{}",age);
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        ReflectionService reflectionService = new ReflectionService();
        //走 int参数方法
        reflectionService.getClass().getMethod("age", Integer.TYPE).invoke(reflectionService,Integer.valueOf("12"));

        //走Integer参数方法
        reflectionService.getClass().getMethod("age", Integer.class).invoke(reflectionService,20);
        reflectionService.getClass().getMethod("age", Integer.class).invoke(reflectionService,Integer.valueOf(20));
    }

}
