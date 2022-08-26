package com.magic.interview.service.reflection;

import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-25 17:57
 **/
public class ReflectService {


    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //默认Scanners为：TypesAnnotated、SubTypes
        //Reflections reflections = new Reflections("com.magic.interview.service.reflection");

        Reflections reflections = new Reflections(ConfigurationBuilder.build("com.magic.interview.service.reflection")
                .addScanners(Scanners.TypesAnnotated));

        //扫描有注解的所有类
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ReAnno.class);
        classes.forEach(c -> {
            String value = c.getAnnotation(ReAnno.class).value();
            System.out.println(value);
        });

        //扫描子类，使用Scanners.SubTypes
        Set<Class<? extends BaseDto>> subTypesOf = reflections.getSubTypesOf(BaseDto.class);
        subTypesOf.forEach(s -> {
            System.out.println("SubTypes: "+s.getName());
        });


        //读取resources 下符合条件的配置文件
        Reflections reflections1 = new Reflections(new ConfigurationBuilder().forPackages("com.magic.interview.service.reflection")
                .addScanners(Scanners.Resources));

        //当前工程resources下的.properties文件
        Set<String> resources = reflections1.getResources(Pattern.compile(".*\\.properties"));
        //Set<String> resources = reflections1.getResources(".*");
        System.out.println(resources);

        //获取读取到的文件内容
        String s = Resources.toString(Resources.getResource(Iterables.getFirst(resources, "")), StandardCharsets.UTF_8);
        System.out.println(s);
        URL resource = ReflectService.class.getClassLoader().getResource(Iterables.getFirst(resources, ""));
        String s1 = Resources.toString(resource, StandardCharsets.UTF_8);


        File file = new File(  "/Users/chengyufei/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar");
        URL url = file.toURI().toURL();
        ClassLoader classLoader = new URLClassLoader(new URL[]{url});
        Class<?> utilClass = classLoader.loadClass("org.apache.commons.lang3.StringUtils");
        //Constructor<?> constructor = utilClass.getConstructor(StringUtils.class);
        Method equalsMethod = utilClass.getMethod("equals", CharSequence.class, CharSequence.class);
        Object invoke = equalsMethod.invoke(equalsMethod,"A", "AA");
        System.out.println(invoke);

    }
}
