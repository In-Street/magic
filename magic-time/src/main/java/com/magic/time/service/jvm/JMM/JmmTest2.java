package com.magic.time.service.jvm.JMM;

/**
 * 验证volatile修饰变量的可见性
 */
public class JmmTest2 {

    volatile boolean flag = false;
    private int value3 = 0;

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            JmmTest2 jmmTest = new JmmTest2();

            new Thread(() -> {
                jmmTest.write();
            }).start();

            new Thread(() -> {
                jmmTest.read();
            }).start();
            Thread.sleep(500);
        }
    }

    public void write()  {
        value3 = 1;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        flag = true;
    }

    public void read() {
        if (!flag) {
            System.out.println(" >>循环中 '");
        }
        System.out.println(">> " + value3);
    }

}
