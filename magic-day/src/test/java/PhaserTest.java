import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Cheng Yufei
 * @create 2024-04-18 18:25
 **/
@Slf4j
public class PhaserTest {


    @Test
    public void phaserEx1() {

        Phaser phaser = new Phaser(10);
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + "  >> AAAA");
            //线程执行到此开始等待，满足条件(任务数满足)则继续执行
            phaser.arriveAndAwaitAdvance();
            System.out.println(Thread.currentThread().getName() + "  >> BBBB");
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(r);
        }

    }

    /**
     *  10 个线程，分两步操作：
     *      第一步 所有线程各自随机一个时间到达A，互相等待 【arriveAndAwaitAdvance】，所有线程到达后执行 onAdvance方法  phase对应0
     *      第二步 所有线程各自随机一个时间 完成任务B，完成后取消注册【arriveAndDeregister】， 所有线程完成后执行 onAdvance phase对应1
     *
     *  awaitAdvance(int phase)：等待直到屏障推进到给定的阶段，如果当前阶段大于或等于给定的阶段，那么此方法将立即返回
     *
     */
    @Test
    public void phaserEx2() throws InterruptedException {


        Phaser phaser = new Phaser(){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {

                int num = registeredParties;
                System.out.println("<<<<<"+num);

                if (Objects.equals(phase,0)) {  //phrase 从0开始
                    System.out.println("<< 所有线程都到达，phase："+phase+"参与数量:  " + num);
                }else if (Objects.equals(phase,1)) {
                    System.out.println("<< 所有线程均已完成任务，phase："+phase+"参与数量:  " + num);
                }

                //
                boolean result = registeredParties == 0;
                System.out.println(" registeredParties == 0  -- "+result);
                return result;  //返回true，表示终止phaser
            }
        };

        Random random = new Random();

        Runnable runnable = () -> {
            try {
                int time = random.nextInt(4000);
                Thread.sleep(time);
                System.out.println(Thread.currentThread().getName() + "  >>到达了A，耗时: " + time);
                phaser.arriveAndAwaitAdvance();

                //全部到达后，开始任务B，每个人完成时间不一样
                int timeCom = random.nextInt(5000);
                Thread.sleep(timeCom);
                System.out.println(Thread.currentThread().getName() + "  >> 完成了任务B，耗时: " + timeCom);
                // phaser.arriveAndAwaitAdvance();
                phaser.arriveAndDeregister();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            phaser.register();
            executorService.execute(runnable);
        }
        // 主线程阻塞一会，让异步线程可以打印内容。放弃使用sleep，采用 isTerminated 方式
        // Thread.sleep(20000);

        while (!phaser.isTerminated()) {
        }
        System.out.println(Thread.currentThread().getName()+"  >> isTerminated 方法外");
    }

    /**
     * 因为当一个Phaser有大量参与者（parties）的时候，内部的同步操作会使性能急剧下降，而分层可以降低竞争，从而减小因同步导致的额外开销。
     *
     * @throws InterruptedException
     */
    @Test
    public void separationPhaser() throws InterruptedException {

        //10 个任务，每3个分一组
        int limit =3,taskNum=10;
        Random random = new Random();

        Phaser parentPhaser = new Phaser(){

            //  If this phaser is a member of a tiered set of phasers, then {@code onAdvance} is invoked only for its root phaser on each advance.
            // 分层 Phaser 时， 子Phaser的 onAdvance 不会执行，因为会直接 invoked 到父Phaser 的 onAdvance 方法
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {

                //所有子Phaser 的 第一阶段完成时执行
                if (0 == phase) {
                    log.info("父Phaser ，phase: {}，   registeredParties: {}", phase, registeredParties); // 父Phaser ，phase: 0，   registeredParties: 4
                }
                //所有子Phaser 的 第二阶段完成时执行
                if (1 == phase) {
                    log.info("父Phaser ，phase: {}，   registeredParties: {}", phase, registeredParties); // 父Phaser ，phase: 1，   registeredParties: 0
                }
                return registeredParties == 0;
                // return super.onAdvance(phase, registeredParties);
            }
        };

        List<Integer> taskList = IntStream.rangeClosed(1, taskNum).boxed().collect(Collectors.toList());// 模拟 10个人
        List<List<Integer>> partition = Lists.partition(taskList, limit); //  每3个分一组，共 4个子Phaser，所以父Phaser的registeredParties=4

        for (int i = 0; i < partition.size(); i++) {

            List<Integer> integers = partition.get(i);

            String name = String.valueOf(i + 1);

            PhaserTask phaserTask = new PhaserTask(random, name,parentPhaser); // 每3个人作为一个 子Phaser
            ExecutorService executorService = Executors.newFixedThreadPool(integers.size());
            for (Integer j : integers) {
                phaserTask.getPhaser().register();
                executorService.execute(phaserTask);
            }
        }

        while (!parentPhaser.isTerminated()) {

        }

        // Thread.sleep(20000);
        System.out.println("  >>>>>>>>>>> 结束 <<<<<<<<<<<<");
    }

    public class PhaserTask implements Runnable {

        @Getter
        private Phaser phaser;
        private Phaser fatherPhaser; // 父级 Phaser
        private Random random;

        private String name;

        public PhaserTask( Random random, String name,Phaser fatherPhaser) {
            this.random = random;
            this.name = name;
            phaser = new Phaser(fatherPhaser) {

                // 子Phaser 的onAdvance 不会执行
                @Override
                protected boolean onAdvance(int phase, int registeredParties) {
                    if (Objects.equals(0,phase)) {
                        log.info("<< 子Phaser 第   {}   分组的所有线程都到达，phase：{}  参与数量:  {}", name, phase, registeredParties);
                    }
                    if (Objects.equals(1,phase)) {
                        log.info("<< 子Phaser 第   {}   分组的所有线程均已完成任务，phase：{}  参与数量:  {}", name, phase, registeredParties);
                    }
                    return super.onAdvance(phase, registeredParties);
                }
            };
        }

        @Override
        public void run() {
            try {
                int time = random.nextInt(4000);
                Thread.sleep(time);
                log.info("{}  >>到达了A，第{}  分组 ， 耗时:{}", Thread.currentThread().getName(), name, time);
                phaser.arriveAndAwaitAdvance();


                int timeCom = random.nextInt(5000);
                Thread.sleep(timeCom);
                log.info("{}  >> 完成了任务B，第{} 分组 ，耗时: {}", Thread.currentThread().getName(), name, timeCom);
                phaser.arriveAndDeregister();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    class SubPhaser extends Phaser {
        private final String name;

        @Getter
        private Integer count=0;

        public SubPhaser(Phaser parent, String name) {
            super(parent);
            this.name = name;
        }

        @Override
        protected boolean onAdvance(int phase, int registeredParties) {
            if (phase == 0 ) { // 当阶段为0且所有参与者已到达
                log.info("子Phaser {}  的所有任务到达，phase: {}, 参与数量:{}", name, phase, registeredParties);
                count++;
            } else if (phase == 1 ) { // 当阶段为1且所有任务已完成
                System.out.println("子Phaser " + name + " 的所有任务完成，phase: " + phase + ", 参与数量: " + registeredParties);
                count++;
            }
            return super.onAdvance(phase, registeredParties);
        }
    }

    /**
     *   Phaser 分层第二版，第一版发现子Phaser的onAdvance的方法不会执行，所以有了这个第二版，onAdvance源码注释已说明子Phaser的onAdvance会invoke到Root
     */
    @Test
    public void separationPhaser_v2() {
        Phaser parentPhaser = new Phaser(){
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                if (phase==0) {
                    log.info("phase: {}, 参与数量:{}",  phase, registeredParties);
                }
                if (phase==1) {
                    log.info("phase: {}, 参与数量:{}",  phase, registeredParties);
                }
                if (phase==2) {
                    log.info("phase: {}, 参与数量:{}",  phase, registeredParties);
                }
                    return super.onAdvance(phase, registeredParties);
            }
        }; // 创建父级Phaser


        // 创建两个子级Phaser，分别代表两个任务组
        SubPhaser subPhaser1 = new SubPhaser(parentPhaser, "Group A");
        SubPhaser subPhaser2 = new SubPhaser(parentPhaser, "Group B");

        // 模拟5个任务，每2个任务作为一个子级Phaser
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            final int taskId = i;
            threads[i] = new Thread(() -> {
                if (taskId < 2) {
                    subPhaser1.register(); // 任务0和1属于Group A

                    try {
                        // 模拟任务执行
                        log.info("subPhaser1 -1  >>  {}", subPhaser1.getRegisteredParties());
                        Thread.sleep((long) (Math.random() * 6000));
                        subPhaser1.arriveAndAwaitAdvance(); // 通知子Phaser并等待阶段推进
                        log.info("subPhaser1 -2  >>  {}", subPhaser1.getRegisteredParties());
                        Thread.sleep((long) (Math.random() * 6000)); // 模拟第二阶段任务
                        log.info("subPhaser1 -3  >>  {}", subPhaser1.getRegisteredParties());
                        subPhaser1.arriveAndDeregister(); // 完成任务并从Phaser注销
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }

                } else {
                    subPhaser2.register(); // 任务2-4属于Group B

                    try {
                        // 模拟任务执行
                        log.info("subPhaser2 -1  >>  {}", subPhaser2.getRegisteredParties());
                        Thread.sleep((long) (Math.random() * 5000));
                        subPhaser2.arriveAndAwaitAdvance(); // 通知子Phaser并等待阶段推进
                        log.info("subPhaser2 -2  >>  {}", subPhaser2.getRegisteredParties());
                        Thread.sleep((long) (Math.random() * 5000)); // 模拟第二阶段任务
                        log.info("subPhaser2 -3  >>  {}", subPhaser2.getRegisteredParties());
                        subPhaser2.arriveAndDeregister(); // 完成任务并从Phaser注销
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threads[i].start();
        }

        // 等待所有任务完成:  采用 thread.join 或 parentPhaser.isTerminated()均可
       /* for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }*/

        while (!parentPhaser.isTerminated()) {

        }

        // 最后，等待所有子Phaser完成并注销自身
        parentPhaser.arriveAndDeregister();
        System.out.println(subPhaser1.getCount()); // 0  因为子Phaser 的 onAdvance 的方法不会执行，会直接invoke到 root的Phaser 的advance
        System.out.println(subPhaser2.getCount());// 0
    }
}
