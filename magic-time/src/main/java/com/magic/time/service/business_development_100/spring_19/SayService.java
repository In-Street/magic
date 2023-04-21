package com.magic.time.service.business_development_100.spring_19;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Cheng Yufei
 * @create 2022-05-15 17:05
 **/
@Slf4j
public abstract class SayService {

    private ArrayList<String> list = new ArrayList<>();

    public void say() {list.add(UUID.randomUUID().toString());
        log.info("我是:{}, size:{}", this, list.size());
    }
}
