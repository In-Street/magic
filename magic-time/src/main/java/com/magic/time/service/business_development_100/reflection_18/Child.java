package com.magic.time.service.business_development_100.reflection_18;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Cheng Yufei
 * @create 2022-04-03 18:36
 **/
@Slf4j
public class Child extends Parent {

    AtomicInteger atomicInteger = new AtomicInteger();


    public void setValue(String value) {
        super.setValue(value);
        log.info("Child setValue：{}", atomicInteger.incrementAndGet());
    }


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Child child = new Child();
        Class<? extends Child> childClass = child.getClass();
        //childClass.getMethod("setValue", String.class).invoke(child, "A");

        //Parent setValue 执行2次，Child setValue 执行1次。
        //两个setValue方法：Child setValue(String)  、Parent经过泛型擦除后的 setValue(Object)
        //Child的setValue方法并没有重写父类方法，被编译器认为是一个新方法
        Arrays.stream(childClass.getMethods()).filter(m -> m.getName().equals("setValue")).forEach(m -> {
            try {
                m.invoke(child, "B");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        //Parent setValue 执行一次，Child setValue 执行1次
        //因为getDeclaredMethods 获取不到父类的方法
        Arrays.stream(childClass.getDeclaredMethods()).filter(m -> m.getName().equals("setValue")).forEach(m -> {
            try {
                m.invoke(child, "B");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

    }
}
