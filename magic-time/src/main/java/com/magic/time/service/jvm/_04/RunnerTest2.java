package com.magic.time.service.jvm._04;

import com.magic.time.service.jvm._05.DevUser;
import org.springframework.core.ResolvableType;

/**
 *
 * @author Cheng Yufei
 * @create 2023-01-15 17:57
 **/
public class RunnerTest2 {

    public static void main(String[] args) throws Exception {
        DevUser devUser = new DevUser();
        String username = devUser.getUsername();
        System.out.println(devUser.getU4(""));

        User user = new DevUser();
        //user.getU2()

        ResolvableType sx = ResolvableType.forField(User.class.getDeclaredField("sx"));
        System.out.println();

    }

    public static void reflectClass() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("com.magic.time.service.jvm._05.DevUser");
        Object o = aClass.newInstance(); //没有直接生成目标类实例，只生成Object实例


        DevUser devUser = new DevUser();
        Class<? extends DevUser> aClass1 = devUser.getClass(); //DevUser 及其子类


        Class<DevUser> devUserClass = DevUser.class;
        DevUser devUser1 = devUserClass.newInstance();


        Class<? super DevUser> superclass = devUserClass.getSuperclass();
    }

}
