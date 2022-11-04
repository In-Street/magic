package com.magic.interview.service.proxy;

import com.magic.dao.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Proxy;

/**
 *
 * @author Cheng Yufei
 * @create 2022-10-14 15:36
 **/
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping("/jdk")
    public void  jdk(){
        UInterface uInterface = new UInterfaceImpl();
      /*  UProxy uProxy = new UProxy();
        uProxy.setObject(uInterface);*/
        UInterface proxyInstance = (UInterface) Proxy.newProxyInstance(uInterface.getClass().getClassLoader(), uInterface.getClass().getInterfaces(), new UProxy(uInterface));
        User aa = new User(1, "AABBCC", "123");
        proxyInstance.check(aa);
    }
}
