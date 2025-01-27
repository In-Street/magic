package com.magic.time.service.jvm.class_load;

/**
 * @author Cheng Yufei
 * @create 2025-01-27 17:22
 **/
public class Child extends Parent{

    static {
        System.out.println(">> Child 初始化");
    }


}
