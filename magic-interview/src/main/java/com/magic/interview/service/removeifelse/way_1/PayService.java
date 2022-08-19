package com.magic.interview.service.removeifelse.way_1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:10
 **/
@Service
@Slf4j
public class PayService {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, IPay> payMap;

    //key默认为首字母小写的类名
   /* @Autowired
    private Map<String, IPay> beans;*/

    @PostConstruct
    public void init() {
        Map<String, Object> annotation = applicationContext.getBeansWithAnnotation(PayWay.class);
        payMap = new HashMap<>();
        annotation.forEach((k, v) -> {
            //转为注解对应的code值
            String code = v.getClass().getAnnotation(PayWay.class).code();
            payMap.put(code, ((IPay) v));
        });
        System.out.println();
    }

    public String pay(String code) {
        //return ((IPay) beans.get(code)).pay();
        return payMap.get(code).pay();
    }
}
