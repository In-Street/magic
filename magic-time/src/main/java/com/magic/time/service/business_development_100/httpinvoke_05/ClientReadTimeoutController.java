package com.magic.time.service.business_development_100.httpinvoke_05;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-27 10:16 PM
 **/
@Slf4j
@RestController
@RequestMapping("/clientReadTimeout")
@EnableFeignClients
public class ClientReadTimeoutController {

    @Autowired
    private Client client;

    public String get() throws IOException {
        Response result = Request.Get("").connectTimeout(2).socketTimeout(3).execute();
        return result.returnContent().toString();
    }

    @PostMapping("/server")
    public void server() throws InterruptedException {
        log.info(">>server接受到请求");
        TimeUnit.SECONDS.sleep(4);
    }

    @GetMapping("/clientRead")
    public void  clientRead(){
        //client.server();
        client.retry();
    }

 ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/serverRetry")
    public void  serverRetry() throws InterruptedException {
        log.info(">>serverRetry 接收到请求");
        TimeUnit.SECONDS.sleep(3);
    }

}
