package com.magic.time.service.jvm.class_load;

/**
 * @author Cheng Yufei
 * @create 2025-01-27 17:22
 **/
public class Parent {

    static {
        System.out.println(" >> parent 初始化");
    }

    static final String parentParam = "父类final静态变量";
    static  String no_final_parent_param = "父类静态变量";

    static  int[] parent_int_array= {1,2};

}
