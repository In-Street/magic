package com.magic.time.service.jvm.JMM;

/**
 * @author Cheng Yufei
 * @create 2025-02-14 16:08
 **/
public class InterruptTest {

    public static void main(String[] args) {
        InterruptTest interruptTest = new InterruptTest();
        Thread thread = interruptTest.handleDownloadTask();
        thread.start();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        thread.interrupt(); // 取消下载任务
    }

    public Thread handleDownloadTask(){

        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(300);
                    System.out.println(">> downloading..... ");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 重新设置中断状态
                    System.out.println(" download cancel");
                    return;
                }

            }
        });
        return thread;
    }
}
