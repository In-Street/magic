package com.magic.interview.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-05 11:10
 **/
@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncController {

    /**
     * WebAsyncManager
     * ThreadPoolTaskExecutor:core-8
     * @return
     */
    @GetMapping("/callable")
    public Callable<String> callbbale() {
        Callable<String> callable = () -> {
            log.info(">>callable 方法处理");
            Thread.sleep(5000);
            return "callbale success";
        };
        return callable;
    }

    @GetMapping("/webAsync")
    public WebAsyncTask<String> webAsync() {

        WebAsyncTask<String> webAsyncTask = new WebAsyncTask<String>(3000, () -> {
            log.info(">>callable 方法处理");
            Thread.sleep(1000);
            throw new RuntimeException("业务异常");
            //return "业务处理";
        });
        webAsyncTask.onCompletion(() -> {
            //超时 或 业务处理 都会执行
            log.info(">>任务完成");
        });

        webAsyncTask.onTimeout(()->{
            log.info(">>任务超时");
            return "超时";
        });

        webAsyncTask.onError(()->{
            log.error(">>任务出错");
            return "异常";
        });

        return webAsyncTask;
    }


    @GetMapping("/mul")
    public String mul() throws IOException {
        for (int i = 0; i < 15; i++) {
            Response execute = Request.Get("http://localhost:8090/async/callable").execute();
            String s = execute.returnContent().toString();
            log.info(">>" + s + " -- " + i);
        }
        return "succ";
    }
}
