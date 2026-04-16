package com.magic.time.service.jvm.optimize.java;

import com.magic.time.service.jvm.class_load.Parent;

/**
 * @author Cheng Yufei
 * @create 2025-01-27 18:45
 **/
public class StringTest extends Parent{

    public static void main(String[] args) {

        // 创建了两个字符串常量池中的常量 "A" 和 "B"（如果之前没有的话）。
        // 在堆中创建两个String对象，内容分别是"A"和"B"。　
        // 然后通过+（实际上是StringBuilder拼接）生成一个新的String对象，内容是"AB"。注意：这个"AB"对象在堆中，并且并没有放入字符串常量池。
        // 所以，s1指向堆中的一个String对象，内容为"AB"。
        String s1 = new String("A") + new String("B");

        //这个方法会检查字符串常量池中是否存在内容为"AB"的字符串。
        // 此时，字符串常量池中并没有"AB"（因为字符串常量池中只有"A"和"B"，而"AB"是运行时生成的，还没有放入常量池）。
        // 那么，intern()方法会将s1这个对象（的引用）放入字符串常量池。注意：在JDK7及以后，字符串常量池在堆中，intern()方法会记录第一次出现的引用，并返回这个引用。
        // 所以，执行完s1.intern()后，字符串常量池中"AB"的引用就是s1所指向的那个对象。
         s1.intern();

        //这里，字符串字面量"AB"会去字符串常量池中查找。
        // 因为上一步已经将s1的引用放入常量池，所以这里s2就是常量池中"AB"的引用，即s2指向的是s1所指向的同一个对象。
        String s2 = "AB";
        System.out.println(s1==s2); // true


        //创建了一个新的String对象，内容为"AB"。注意：这里的"AB"字符串字面量在常量池中已经存在（即s1.intern()放入的），所以不会在常量池中新建"AB"，但是new会在堆中创建一个新的String对象（不同于s1指向的对象）。
        // 所以，s3指向堆中的一个新String对象，内容也是"AB"
        String s3 = new String("AB");

        //此时，字符串常量池中已经存在"AB"（引用是s1），所以这个调用会返回常量池中已存在的引用（即s1），但是对s3本身没有影响（s3还是指向自己新建的对象）。
        // 注意：intern()方法返回的是常量池中的引用，但这里我们没有使用返回值。所以s3.intern()只是查询了一下，并没有改变s3的引用。
        s3.intern();
        System.out.println(s3==s2); // false


        String s4 = new String("AB");
        s4 = s4.intern();
        System.out.println(s4==s2);

        //注意：上面的分析基于JDK7及以后的版本（字符串常量池在堆中，且intern()方法会将堆中对象的引用放入常量池）。在JDK6及以前，字符串常量池在方法区，intern()方法会把字符串复制到常量池，所以结果可能会不同。
        // 在JDK6中，第一段代码会输出false，因为s1.intern()会将"AB"字符串复制到常量池，然后s2指向的是常量池中的"AB"，而s1指向的是堆中的对象，不是同一个。第二段也会是false。

    }

}
