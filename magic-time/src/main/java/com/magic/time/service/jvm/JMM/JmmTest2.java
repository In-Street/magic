package com.magic.time.service.jvm.JMM;

import com.magic.time.service.jvm.class_load.Parent;

/**
 * 验证volatile修饰变量的可见性
 */
public class JmmTest2 {

    volatile boolean flag = false;
    private int value3 = 0;

     volatile Parent parent = new Parent(123);

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

        System.out.println("---------------------------------------------------------------------------------------------------");

        JmmTest2 jmmTest = new JmmTest2();
        System.out.println(jmmTest.parent);

        new Thread(() -> {
            jmmTest.writeParent(); //  volatile 修饰引用类型的意义，参考xmind
        }).start();

        Thread.sleep(10);
        System.out.println(jmmTest.parent);

        new Thread(() -> {
            jmmTest.readParent();
            System.out.println(jmmTest.parent);
        }).start();


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

    public void writeParent()  {
        parent = new Parent(456);
        // parent.setValue(11111);
        System.out.println(">>修改成功");
    }

    public void readParent()  {
        System.out.println(parent.getValue());
    }


}
