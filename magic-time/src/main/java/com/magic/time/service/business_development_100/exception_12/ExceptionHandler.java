package com.magic.time.service.business_development_100.exception_12;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-05 5:41 PM
 **/
@Slf4j
public class ExceptionHandler {

    /**
     * finally异常覆盖业务主异常
     */
    public static void h1() {
        try {
            throw new RuntimeException(">> try exception");
        } finally {
            throw new RuntimeException(">> finally exception");
        }
    }

    /**
     * 使用静态变量异常，异常栈信息错乱
     */
    public static void h2() {
        try {
            create();
        } catch (Exception e) {
            log.error(">>>创建订单异常", e);
        }

        try {
            cancel();
        } catch (Exception e) {
            log.error(">>>取消订单异常", e);
        }
    }

    private static void create() {
        throw Exceptions.commonException;
        //throw Exceptions.commonException2();
    }

    private static void cancel() {
        throw Exceptions.commonException;
        //throw Exceptions.commonException2();
    }


    public static void h3() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("pool_thread_%s")
                .setUncaughtExceptionHandler((thread, exception) -> {
                    log.error(String.format(">>自定义线程池异常，thread:%s", thread), exception);
                })
                .build());

        ArrayList<Future> futures = new ArrayList<>();
        IntStream.rangeClosed(1, 6).forEach(i ->
                {
                    //Future<?> future = executorService.submit(() -> {
                    executorService.execute(() -> {
                            if (i == 5) {
                                throw new RuntimeException("任务5失败");
                            } else {
                                log.info("任务：{} 已完成", i);
                            }

                    });
                    //futures.add(future);
                }
        );

        futures.stream().forEach(f->{
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("获取结果中的异常，",e);
            }
        });
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        h3();
    }
}
