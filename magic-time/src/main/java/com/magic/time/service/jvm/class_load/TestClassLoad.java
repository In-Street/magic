package com.magic.time.service.jvm.class_load;


import java.lang.reflect.Field;

/**
 * @author Cheng Yufei
 * @create 2025-01-27 17:25
 **/
public class TestClassLoad {

    public static void main(String[] args) throws NoSuchFieldException, InstantiationException, IllegalAccessException {
        // 访问final修饰的 基本类型/字符串 不会触发类的初始化
        // System.out.println(Child.parentParam);

        // 子类访问父类的非 final静态变量会触发父类的初始化，不会触发子类的初始化
        // System.out.println(Child.no_final_parent_param);

        //数组定义类，不会触发类的初始化
        // Child[] childArray = new Child[2];

        //单纯的获取Field 不会触发初始化。 调用get访问静态字段会触发初始化【即使是final修饰的基本类型/字符串静态字段】
        Field declaredField = Parent.class.getDeclaredField("no_final_parent_param");
        System.out.println(declaredField.get(null));

        //单纯的获取引用，不会触发初始化
        Class<Parent> parentClass = Parent.class;
        System.out.println(parentClass.getName());
        // Parent parent = parentClass.newInstance();


    }
}
