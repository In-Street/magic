package com.magic.time.service.jvm._04;

import com.magic.time.service.jvm._05.DevUser;

import java.lang.reflect.Method;

/**
 *
 * @author Cheng Yufei
 * @create 2023-01-15 17:57
 **/
public class RunnerTest {

    public static void main(String[] args) throws Exception {

        System.out.println(DevUser.getU());

        System.out.println(User.getU());

        System.out.println(t());

        Class<?> aClass = Class.forName("com.magic.time.service.jvm._04.RunnerTest$Test2");
        Method me = aClass.getMethod("target", int.class);
        //关闭权限检查
        me.setAccessible(true);
        //循环外创建Integer对象，避免频繁GC
        Integer param = 128;

        //Object[] objects = {128};

        long current = System.currentTimeMillis();
        for (int i = 1; i <= 2_000_000_000; i++) {
            if (i % 100_000_000 == 0) {
                long temp = System.currentTimeMillis();
                System.out.println(temp - current);
                current = temp;
            }
            //以不调用时的时间为基准：87ms

            me.invoke(null, param);
            //me.invoke(null, 128);
            //me.invoke(null, objects);
        }
    }

    public static String t() {

        String res = "";
        try {
            //System.out.println("try");
            Integer[] integers = {1};
            //System.out.println(integers[1]);
            res = res + "try ";
        } catch (ArrayIndexOutOfBoundsException e) {
            //System.out.println("cache");
            res = res + "cache ";
        } finally {
            System.out.println("进入finally");
            res = res + "finally";
        }
        return res;
    }

    public static class Test2 {

        public static void target(int i) {
            //new Exception("#" + i).printStackTrace();
        }
    }
}
