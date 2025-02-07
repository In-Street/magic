package com.magic.time.service.jvm.JMM;

/**
 * @author Cheng Yufei
 * @create 2025-02-07 16:32
 **/
public class JmmTest {

    static int value = 0;
    static int value2 = 0;


    public static void main(String[] args) throws InterruptedException {

        // happens-before ： Thread.start() 先于线程内的任何操作。 子线程能看到主线程在start()之前的修改
        Thread thread = new Thread(() -> {
            System.out.println(value);
        });
        value = 100;
        thread.start();

        // happens-before ：线程的所有操作先于其他线程检测到该线程的终止。主线程可以看到子线程的修改
        Thread thread2 = new Thread(() -> {
            value2 = 201;
        });
        thread2.start();
        thread2.join();
        System.out.println(value2);

    }

}
