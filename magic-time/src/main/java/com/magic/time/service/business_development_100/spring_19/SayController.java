package com.magic.time.service.business_development_100.spring_19;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cheng Yufei
 * @create 2022-05-15 17:11
 **/
@RestController
@RequestMapping("/say")
public class SayController {

    @Autowired
    private HelloService helloService;
    @Autowired
    private HiService hiService;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/say")
    public String say() {
        helloService.hello();
        return "Success";
    }

    @GetMapping("/say2")
    public void say2() {
        //hiService.hi();
        applicationContext.getBean(HiService.class).hi();
    }
}
