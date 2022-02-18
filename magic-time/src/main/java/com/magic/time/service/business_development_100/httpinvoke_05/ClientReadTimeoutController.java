package com.magic.time.service.business_development_100.httpinvoke_05;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

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
    @Autowired
    private RestTemplate restTemplate;

    public String get() throws IOException {
        Response result = Request.Get("").connectTimeout(2).socketTimeout(3).execute();
        return result.returnContent().toString();
    }

    @PostMapping("/server")
    public String server() throws InterruptedException {
        log.info(">>server接受到请求");
        TimeUnit.SECONDS.sleep(2);
        return "success";
    }

    @GetMapping("/clientRead")
    public void clientRead() {
        //client.server();
        client.retry();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/serverRetry")
    public void serverRetry() throws InterruptedException {
        log.info(">>serverRetry 接收到请求");
        TimeUnit.SECONDS.sleep(3);
    }

    @GetMapping("/clientReadConcurrent")
    public void clientReadConcurrent(@RequestParam Integer count) throws InterruptedException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        AtomicInteger atomicInteger = new AtomicInteger();
        StopWatch stopWatch = new StopWatch("123");
        stopWatch.start("并发数测试");
        IntStream.rangeClosed(1, count).forEach(i -> {
            //threadPool.execute(() -> client.server());
            threadPool.execute(() ->restTemplate.postForObject(URI.create("http://localhost:9090/clientReadTimeout/server"),null,String.class));
            atomicInteger.addAndGet(1);
        });
        threadPool.shutdown();
        threadPool.awaitTermination(5, TimeUnit.MINUTES);
        stopWatch.stop();
        log.info(">>发送请求:{}次，耗时：{} ms",atomicInteger.get(),stopWatch.getTotalTimeMillis());
    }
}
