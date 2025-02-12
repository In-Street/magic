package com.magic.time.service.jvm.class_load;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Cheng Yufei
 * @create 2025-01-27 17:25
 **/
public class TestClassLoad {

    public static void main(String[] args) throws NoSuchFieldException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        // 访问final修饰的 基本类型/字符串 不会触发类的初始化
        System.out.println(Child.parentParam);

        // 子类访问父类的非 final静态变量会触发父类的初始化，不会触发子类的初始化
        // System.out.println(Child.no_final_parent_param);

        //数组定义类，不会触发类的初始化
        Child[] childArray = new Child[2];

        //单纯的获取Field 不会触发初始化。 调用get访问静态字段会触发初始化【即使是final修饰的基本类型/字符串静态字段】
        Field declaredField = Parent.class.getDeclaredField("no_final_parent_param");
        // System.out.println(declaredField.get(null));

        //单纯的获取引用，不会触发初始化
        Class<Parent> parentClass = Parent.class;
        System.out.println(parentClass.getName());

        // newInstance： 会触发类的初始化
        // Parent parent = parentClass.newInstance();

        //会触发类的初始化
        Class<?> aClass = Class.forName("com.magic.time.service.jvm.class_load.Child");


        System.out.println("------------------------------------------------------------------------------");
        Child child = new Child();
        String[] strArray = child.getStrArray();
        Arrays.stream(strArray).forEach(System.out::println);
        strArray = new String[]{"CC"};
        Arrays.stream(strArray).forEach(System.out::println);

        child.getStrList().add("List");
        System.out.println(child.getStrList());

        // 十进制 -> 二进制
        System.out.println(Integer.toBinaryString(5)); // 101

        // 二进制 -> 十进制
        System.out.println(Integer.parseInt("101",2)); //  1*2^0 + 0 + 1*2^2 = 5

        String key = "A"; int h;
        int hashCode  = (h = key.hashCode()) ^ (h >>> 16); // HashMap计算hash值
        System.out.println(">> hashCode:  "+hashCode);
        System.out.println(hashCode%16);
        System.out.println((hashCode&16) == 0 ); // HashMap扩容：  元素hash值 & oldCapacity ==0 则 新位置=原索引，否则 新位置 = 原索引 + oldCapacity

    }
}
