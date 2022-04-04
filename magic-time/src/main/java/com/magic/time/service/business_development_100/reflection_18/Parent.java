package com.magic.time.service.business_development_100.reflection_18;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Cheng Yufei
 * @create 2022-04-03 18:33
 **/
@Slf4j
@ToString
public class Parent<T> {

    AtomicInteger atomicInteger = new AtomicInteger();
    private T value;

    public void setValue(T value) {

        this.value = value;
        log.info("Parent setValue：{}, {}", atomicInteger.incrementAndGet(), this);
    }
}
