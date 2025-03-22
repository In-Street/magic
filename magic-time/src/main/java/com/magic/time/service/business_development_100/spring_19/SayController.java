package com.magic.time.service.business_development_100.spring_19;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    @Autowired
    @Qualifier("otherThreadPool")
    private ThreadPoolTaskExecutor executor;

    @GetMapping("/say")
    public String say() {
        // helloService.hello();

        //  execute 方法执行任务，可通过 setUncaughtExceptionHandler 自定义处理；
        executor.execute(() -> {
            int a = 0;
            int b = 100 / a;
        });

        // submit 方法执行任务，异常是包装在 Future对象中的，Future # get 显式获取
        Future<String> future = executor.submit(() -> {
            int a = 0;
            int b = 100 / a;
            return "Ok";
        });
        try {
            String result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return "Success";
    }

    @GetMapping("/say2")
    public void say2() {
        //hiService.hi();
        applicationContext.getBean(HiService.class).hi();
    }
}
