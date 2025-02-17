package com.magic.time.service.jvm.AQS;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Cheng Yufei
 * @create 2025-02-17 12:02
 **/
public class ListTest {

    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("AAA");

        Iterator<String> iterator = list.iterator(); // 基于数据快照
        list.add("BBB");
        while (iterator.hasNext()) {
            list.add("CCC"); // 在迭代期间仍可对数据写操作，不会抛出ConcurrentModificationException
            System.out.println(iterator.next()); // 看不到 BBB、CCC
        }

        ArrayList<String> strings = new ArrayList<>();
        strings.add("aaa");
        Iterator<String> iterator1 = strings.iterator();
        strings.add("bbb"); // 抛异常：java.util.ConcurrentModificationException
        while (iterator1.hasNext()) {
            strings.add("bbb"); // java.util.ConcurrentModificationException
            System.out.println(iterator1.next());
        }

    }
}
