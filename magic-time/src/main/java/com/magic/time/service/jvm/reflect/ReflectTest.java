package com.magic.time.service.jvm.reflect;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Cheng Yufei
 * @create 2025-01-31 11:17
 **/
public class ReflectTest {

    public static void main(String[] args) throws Throwable {
        Child child = new Child();

        Class<Child> childClass = Child.class;

        // 获取当前类及父类中的所有public方法
        Method[] methods = childClass.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        Class<? super Child> superclass = childClass.getSuperclass();

        //获取当前类中的所有方法
        Method[] methods1 = superclass.getDeclaredMethods();
        System.out.println(">>> 父类：");
        for (Method field : methods1) {
            System.out.println(field.getName());
        }

        Method parentMethod = childClass.getMethod("parentMethodPublic", String.class);
        // -Dsun.reflect.inflationThreshold=15    反射调用超过15次，委托实现转为动态实现
        //  -Dsun.reflect.noInflation=true , 首次调用就开始动态实现
        for (int i = 0; i < 15; i++) {
             // Object invokeResult = parentMethod.invoke(child, "反射调用方法"+i);
        }

        System.out.println(" >>>>  MethodHandles <<<<");
        // 使用MethodHandles 替代反射，接近于直接调用
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        // findVirtual三个参数： 目标方法所属的类，目标方法名，方法的返回类型和参数类型
        MethodHandle parentMethodPublic = lookup.findVirtual(Child.class, "parentMethodPublic", MethodType.methodType(String.class,String.class));
        for (int i = 0; i < 16; i++) {
            // 调用方法
            // parentMethodPublic.invoke(child,"反射方法调用");
        }



        // MethodHandles: 访问公共成员变量
        Parent parent = new Parent();
        parent.setP2(123);
        MethodHandle p1 = lookup.findGetter(Parent.class, "p2", int.class);
        Object invoke = p1.invoke(parent);
        System.out.println(invoke);

        MethodHandle p2 = lookup.findSetter(Parent.class, "p2", int.class);
        p2.invoke(parent, 567);
        System.out.println(parent.getP2());


        // MethodHandles: 访问private成员变量
        Class<Parent> parentClass = Parent.class;
        Field privateField = parentClass.getDeclaredField("p1");
        privateField.setAccessible(true);

        MethodHandle getterPrivate = lookup.unreflectGetter(privateField);
        MethodHandle setterPrivate = lookup.unreflectSetter(privateField);
        setterPrivate.invoke(parent, 666);
        System.out.println(">> 私有成员变量："+getterPrivate.invoke(parent));
    }
}