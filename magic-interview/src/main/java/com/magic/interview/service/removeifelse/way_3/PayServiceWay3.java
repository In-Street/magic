package com.magic.interview.service.removeifelse.way_3;

import com.google.common.collect.Lists;
import com.magic.interview.service.removeifelse.way_1.IPay;
import com.magic.interview.service.removeifelse.way_1.PayWay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:10
 **/
@Service
@Slf4j
public class PayServiceWay3 {

    @Autowired
    private ApplicationContext applicationContext;

    private PayHandler payHandler;


    @PostConstruct
    public void init() {
        Map<String, PayHandler> beans = applicationContext.getBeansOfType(PayHandler.class);
        List<PayHandler> handlers = Lists.newArrayList(beans.values());
        for (int i = 0; i < handlers.size(); i++) {
            if (i + 1 >= handlers.size()) {
                break;
            }
            handlers.get(i).setNext(handlers.get(i + 1));
        }
        payHandler = handlers.get(0);
        System.out.println();
    }

    public String pay(String code) {
        return payHandler.pay(code);
    }
}
