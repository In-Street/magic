package com.magic.time.service.business_development_100.reflection_18;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Cheng Yufei
 * @create 2022-04-03 18:36
 **/
@Slf4j
public class Child2 extends Parent<String> {

    AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 指定Parent的泛型类型并添加@Override
     * @param value
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
        log.info("Child setValue：{}", atomicInteger.incrementAndGet());
    }


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Child2 child = new Child2();
        Class<? extends Child2> childClass = child.getClass();
        //childClass.getMethod("setValue", String.class).invoke(child, "A");

        //Parent setValue 执行2次，Child setValue 执行2次
        //两个setValue方法： Child setValue(String) 、 Child setValue(Object) 【编译器生成的桥接方法，内部调用String入参的setValue】
        Arrays.stream(childClass.getMethods()).filter(m -> m.getName().equals("setValue")).forEach(m -> {
            try {
                m.invoke(child, "B");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        //Parent setValue 执行2次，Child setValue 执行2次
        Arrays.stream(childClass.getDeclaredMethods()).filter(m -> m.getName().equals("setValue")).forEach(m -> {
            try {
                m.invoke(child, "B");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        Arrays.stream(childClass.getDeclaredMethods()).filter(m -> m.getName().equals("setValue") && !m.isBridge()).forEach(m -> {
            try {
                m.invoke(child, "B");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
