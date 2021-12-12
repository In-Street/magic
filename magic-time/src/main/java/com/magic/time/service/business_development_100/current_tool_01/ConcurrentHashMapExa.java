package com.magic.time.service.business_development_100.current_tool_01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-11 4:36 PM
 **/
@Slf4j
public class ConcurrentHashMapExa {

    public static final int threadCount = 10;
    public static final int total = 1000;

    public static Object lock = new Object();

    /**
     *有一个含 900 个元素的 Map，现在再补充 100 个元素进去，这个补充操作由 10 个线程并发进行;
     * 结论：size、isEmpty、containsValue等方法，并发情况下有中间状态，计算结果存在差异，仍需用锁来保证线程安全结果正确；
     */
    public static void situation() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        IntStream.rangeClosed(1, 900).forEach(i -> map.put(UUID.randomUUID().toString(), ThreadLocalRandom.current().nextInt(1000)));
        log.info("初始化map，容量：{}", map.size());

        //parallel 假并发数，实际为机器核心线程数-1
        IntStream.rangeClosed(1, 10).parallel().forEach(thread -> {
            synchronized (lock) {
                int remain = total - map.size();
                log.info("应补充个数:{}", remain);
                IntStream.rangeClosed(1, remain).forEach(i -> map.put(UUID.randomUUID().toString(), ThreadLocalRandom.current().nextInt(1000)));
            }
        });
        log.info("最后map 容量：{}", map.size());

    }

    /**
     * parallel：使用ForkJoinPool.commonPool，默认线程数为:Runtime.getRuntime().availableProcessors() -1 。【线程名：ForkJoinPool.commonPool-worker-xxx】
     * 若要提高并发量/指定并发量，两种方式：
     *      A：System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","12") ，全局设置【不推荐】
     *      B:  使用 ForkJoinPool forkJoinPool = new ForkJoinPool(20) ; 【线程名：ForkJoinPool-1-worker-xxx】
     *
     *
     * @throws InterruptedException
     */
    public static void situationByForkJoinPool() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        IntStream.rangeClosed(1, 900).forEach(i -> map.put(UUID.randomUUID().toString(), ThreadLocalRandom.current().nextInt(1000)));
        log.info("初始化map，容量：{}", map.size());

        //指定并发数
        ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        forkJoinPool.submit(() -> {
            IntStream.rangeClosed(1, 10).parallel().forEach(thread -> {
                synchronized (lock) {
                    int remain = total - map.size();
                    log.info("应补充个数:{}", remain);
                    IntStream.rangeClosed(1, remain).forEach(i -> map.put(UUID.randomUUID().toString(), ThreadLocalRandom.current().nextInt(1000)));
                }
            });
        });
        //等待所有任务完成
        forkJoinPool.shutdown();
        //没有shutdown()时，需等待5min才能结束方法；有shutdown()时，若任务完成可直接结束，无需强制阻塞5min；
        forkJoinPool.awaitTermination(5, TimeUnit.MINUTES);

        log.info("最后map 容量：{}", map.size());
    }

    /**
     *使用 ConcurrentHashMap 来统计Key 出现次数，Key 的范围是 10;
     * 结论： computeIfAbsent 属于原子性操作方法，采用java自带的Unsafe实现CAS确保写入数据的原子性；此时无需加锁来保证
     *
     * ConcurrentHashMap类中原子性方法：computeIfAbsent 、merge、computeIfPresent、compute
     */
    public static void goodCompute() {

        ConcurrentHashMap<Integer, LongAdder> map = new ConcurrentHashMap<>();
        IntStream.rangeClosed(1, 10000).parallel().forEach(i -> {
            map.computeIfAbsent(ThreadLocalRandom.current().nextInt(10), k -> new LongAdder()).increment();
        });
        map.forEach((k, v) -> System.out.println(k + " >> " + v.intValue()));
        System.out.println("次数总和： " + map.entrySet().stream().mapToInt(m -> m.getValue().intValue()).reduce(Integer::sum));
    }

    /**
     * 加锁保证正确结果，ConcurrentHashMap的containsKey、put 方法非原子性操作
     */
    public static void badCompute() {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        IntStream.rangeClosed(1, 10000).parallel().forEach(i -> {
            synchronized (map) {
                int key = ThreadLocalRandom.current().nextInt(10);
                if (map.containsKey(key)) {
                    map.put(key, map.get(key) + 1);
                } else {
                    map.put(key, 1);
                }
            }
        });
        map.forEach((k, v) -> {
            System.out.println(k + " >> " + v);
        });
        System.out.println("次数总和： " + map.entrySet().stream().mapToInt(m -> m.getValue()).reduce(0, Integer::sum));
    }

    /**
     *                   ---------------------------------------------
     *                      ns                  %     Task name
     *                   ---------------------------------------------
     *                      037192792  081%     bad
     *                      008613333  019%     good
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        // 利用StopWatch 查看两种方式性能
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("bad");
        badCompute();
        stopWatch.stop();

        stopWatch.start("good");
        goodCompute();
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());

        //badCompute();
        //goodCompute();
        //situation();
        //situationByForkJoinPool();
    }
}
