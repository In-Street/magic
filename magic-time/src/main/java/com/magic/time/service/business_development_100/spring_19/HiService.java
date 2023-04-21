package com.magic.time.service.business_development_100.spring_19;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cheng Yufei
 * @create 2022-05-15 17:11
 **/
@Service
//不设置proxyMode时，需要使用ApplicationContext获取
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class HiService extends SayService{

    public void hi() {
        super.say();
    }
}
