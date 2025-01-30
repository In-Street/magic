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
    static String no_final_parent_param = "父类静态变量";

    /**
     *  final 修饰的成员变量：
     *      基本类型、字符串： 值不可变， 重新赋值时会编译错误；
     *
     *      引用类型：引用不能指向另一个对象，但对象内部的状态可以改变；
     */
    final int basic_int = 10;
    final String basic_string = "AAA";
    final StringBuilder sb = new StringBuilder("CCC");



    static int[] parent_int_array = {1, 2};

    protected   Object getA(Integer a) {
        return "Parent getA";
    }
}
