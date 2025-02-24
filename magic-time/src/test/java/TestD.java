import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *   两个线程交替打印
 *
 * @author Cheng Yufei
 * @create 2025-02-24 11:28
 **/
public class TestD {

    /**
     *  失败示例
     */
    @Test
    public void t1() {
        Thread[] threads = new Thread[2];
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            if (i % 2 == 0) {
                threads[0] = new Thread(() -> {
                    System.out.println(Thread.currentThread().getId() + "  " + finalI);
                });
                threads[0].start();
                continue;
            }
            threads[1] = new Thread(() -> {
                System.out.println(Thread.currentThread().getId() + "  " + finalI);
            });
            threads[1].start();
        }

    }

    /**
     *  失败示例
     *   多线程环境下，由于线程调度的不确定性，可能会出现一个线程连续多次执行成功 compareAndSet , 从而导致连续打印
     *   F CAS 成功，打印一次
     *   切换到S，S CAS 成功后还未打印就直接调度为F线程
     *    F 又可以CAS成功，打印一次，造成连续打印
     *
     */
    @Test
    public void t2() throws InterruptedException {
        AtomicBoolean state = new AtomicBoolean();
        AtomicInteger count = new AtomicInteger();
        new Thread(() -> {
            while (count.get() < 10) {
                if (state.compareAndSet(false, true)) {
                    System.out.println(state.get() + " - " + Thread.currentThread().getName() + " >> " + count.getAndIncrement());
                }
            }
        }, "F").start();

        new Thread(() -> {
            while (count.get() < 10) {
                if (state.compareAndSet(true, false)) {
                    System.out.println(state.get() + " - " + Thread.currentThread().getName() + " >> " + count.getAndIncrement());
                }
            }
        }, "S").start();
    }

    /**
     *  正确示例
     */
    @Test
    public void t3() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        final int[] count = {0};
        final boolean[] state = {false};

        Thread threadF = new Thread(() -> {
            while (count[0] < 10) {
                try {
                    lock.lock();
                   /* if (!state[0]) {
                        conditionA.await();
                    }*/
                    while (!state[0]) {
                        conditionA.await();
                    }
                    System.out.println(Thread.currentThread().getName() + " >> " + count[0]);
                    count[0]++;
                    state[0] = false;
                    conditionB.signal();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                        lock.unlock();
                }
            }
        }, "F");


        Thread threadS = new Thread(() -> {
            while (count[0] < 10) {
                try {
                    lock.lock();
                 /*   if (state[0]) {
                        conditionB.await();
                    }*/
                    while (state[0]) {
                        conditionB.await();
                    }
                    System.out.println(Thread.currentThread().getName() + " >> " + count[0]);
                    count[0]++;
                    state[0] = true;
                    conditionA.signal();

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                        lock.unlock();
                }
            }
        }, "S");

        threadF.start();
        threadS.start();

        threadF.join();
        threadS.join();
    }

    /**
     *  正确示例
     */
    @Test
    public void t4() throws InterruptedException {
        Object lock = new Object();
        final int[] count = {0};
        final boolean[] state = {false};

        Thread threadF = new Thread(() -> {
            while (count[0] < 10) {
                    synchronized (lock){
                        if (!state[0]) {
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        System.out.println(Thread.currentThread().getName() + " >> " + count[0]);
                        count[0]++;
                        state[0] = false;
                        lock.notify();
                    }
            }
        }, "F");

        Thread threadS = new Thread(() -> {
            while (count[0] < 10) {
                synchronized (lock){
                    if (state[0]) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " >> " + count[0]);
                    count[0]++;
                    state[0] = true;
                    lock.notify();
                }
            }
        }, "S");

        threadF.start();
        threadS.start();

        threadF.join();
        threadS.join();

    }
}
