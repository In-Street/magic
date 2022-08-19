package com.magic.interview.service.removeifelse.way_2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:10
 **/
@Service
@Slf4j
public class PayServiceWay2_2 implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    private List<IPayWay2> list;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, IPayWay2> beans = applicationContext.getBeansOfType(IPayWay2.class);
        list = new ArrayList<>();
        beans.forEach((k,v)->{
            list.add(v);
        });
    }

    public String pay(String code) {
        Optional<IPayWay2> first = list.stream().filter(p -> p.support(code)).findFirst();
        if (!first.isPresent()) {
            return "不存在";
        }
        return first.get().pay();
    }
}
