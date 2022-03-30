package com.magic.time.service.business_development_100.oom_17;

import com.magic.time.dao.model.OrgInfo;
import com.magic.time.dao.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 *
 * @author Cheng Yufei
 * @create 2022-03-23 10:11 PM
 **/
@Slf4j
public class OomService {

   static Map<UserInfo, OrgInfo> cache = new WeakHashMap<>();
    static ConcurrentReferenceHashMap<UserInfo, OrgInfo> cache2 = new ConcurrentReferenceHashMap<>(1000000, ConcurrentReferenceHashMap.ReferenceType.WEAK);



    public static void o1() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                log.info("缓存容量：{}",cache2.size());
        }, 1, 1, TimeUnit.SECONDS);

        String username = "Rihana";

        //对UserInfo强引用：容量不变
       /* IntStream.rangeClosed(1, 1000000).forEach(i -> {
            UserInfo userInfo = new UserInfo(i, username + i);
            OrgInfo orgInfo = new OrgInfo();
            orgInfo.setUserInfo(userInfo);
            cache.put(userInfo, orgInfo);
        });*/

        //对UserInfo弱引用：容量可变
        /*IntStream.rangeClosed(1, 1000000).forEach(i -> {
            UserInfo userInfo = new UserInfo(i, username + i);
            OrgInfo orgInfo = new OrgInfo();
            orgInfo.setWeakUserInfo(new WeakReference<>(userInfo));
            cache.put(userInfo, orgInfo);
        });*/


          IntStream.rangeClosed(1, 1000000).forEach(i -> {
            UserInfo userInfo = new UserInfo(i, username + i);
            OrgInfo orgInfo = new OrgInfo();
            orgInfo.setUserInfo(userInfo);
            cache2.put(userInfo, orgInfo);
        });
    }

    public static void main(String[] args) throws InterruptedException {
        o1();
        Thread.sleep(5000);
        System.out.println(">> GC <<");
        System.gc();


    }
}
