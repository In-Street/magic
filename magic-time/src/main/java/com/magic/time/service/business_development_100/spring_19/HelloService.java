package com.magic.time.service.business_development_100.spring_19;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cheng Yufei
 * @create 2022-05-15 17:08
 **/
@Service
//设置代理才会在单例的controller中使此service的scope生效
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class HelloService extends SayService {

    public void hello() {
        super.say();
    }
}
