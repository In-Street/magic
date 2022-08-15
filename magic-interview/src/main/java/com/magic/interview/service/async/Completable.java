package com.magic.interview.service.async;

import com.magic.interview.service.jackson.customDeserial.UserTest;
import com.magic.interview.service.lambda_exception.Handle;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-12 14:21
 **/
public class Completable {


    public static void t1() throws ExecutionException, InterruptedException {

        CompletableFuture<UserTest> futureA = CompletableFuture.supplyAsync(() -> {
            UserTest userTest = new UserTest();
            userTest.setUsername("A");
            return userTest;
        });

        //1.串行：thenApply、thenAccept、thenRun、thenCompose

        //thenApply:结果转换 ， 接受Function，处理上一个CompletableFuture的调用结果，返回同一个CompletableFuture
        Function<UserTest, UserTest> function = user -> {
            user.setAddress("By Function");
            return user;
        };
        CompletableFuture<UserTest> thenApplyResult = futureA.thenApply(function);
        System.out.println("thenApply " + thenApplyResult.get());


        //thenAccept: 结果消费 ， 接受Consumer
        Consumer<UserTest> consumer = user -> {
            user.setAddress("By Consumer");
        };
        CompletableFuture<Void> thenAcceptResult = futureA.thenAccept(consumer);

        //thenAcceptBoth: 结果消费，当两个CompletableFuture都正常完成计算时候，执行提供的action消费两个异步的结果。
        CompletableFuture<Void> thenAcceptBothResult = futureA.thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            return "第二个CompletableFuture，用来thenAcceptBoth";
        }), (userTest, string) -> {
            userTest.setAddress("thenAcceptBoth:" + string);
        });
        //System.out.println("thenAcceptBoth " + thenAcceptBothResult.get());


        //thenRun:结果消费，接收Runnable
        futureA.thenRun(() -> {
            try {
                System.out.println(futureA.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        //thenCompose(): 结果转换 ， 使用上一个CompletableFuture的结果，在下一个CompletableFuture中计算，返回新的CompletableFuture
        CompletableFuture<UserTest> thenComposeResult = futureA.thenCompose(new Function<UserTest, CompletionStage<UserTest>>() {
            @Override
            public CompletionStage<UserTest> apply(UserTest userTest) {
                return CompletableFuture.supplyAsync(() -> {
                    userTest.setAa("By thenCompose");
                    return userTest;
                });
            }
        });
        System.out.println("thenCompose " + thenComposeResult.get());


        //2. AND汇聚：结果组合 thenCombine \ thenAcceptBoth
        CompletableFuture<UserTest> combineFuture = futureA.thenCombine(CompletableFuture.supplyAsync(() -> {
            return "第二个CompletableFuture，用来combine";
        }), (userTest, string) -> {
            userTest.setAddress("By Combine: " + string);
            return userTest;
        });
        System.out.println("combineFuture " + combineFuture.get());


        //3. OR汇聚： applyToEither \ acceptEither \ runAfterEither

        //applyToEither: 两个线程任务获取结果速度相比较，先获得结果的对该结果执行下一步操作
        CompletableFuture<String> either1 = CompletableFuture.supplyAsync(Handle.handleSupplier(() -> {
            int i = ThreadLocalRandom.current().nextInt(10);
            Thread.sleep(i * 100);
            return "第一个处理结果:" + i;
        }));
        CompletableFuture<String> either2 = CompletableFuture.supplyAsync(Handle.handleSupplier(() -> {
            int i = ThreadLocalRandom.current().nextInt(10);
            Thread.sleep(i * 100);
            return "第二个处理结果:" + i;
        }));
        CompletableFuture<String> applyToEitherResult = either1.applyToEither(either2, s -> {
            return s + " 先执行完，对结果进行下一步操作";
        });
        //System.out.println(applyToEitherResult.get());

        //acceptEither: 对先执行完的结果进行消费
        CompletableFuture<Void> acceptEitherResult = either1.acceptEither(either2, s -> {
            System.out.println("acceptEitherResult " + s + " 先执行完，对我的结果进行消费");
        });

        //runAfterEither
        either1.runAfterEither(either2, () -> {
            System.out.println("runAfterEither : 已经有一个任务执行完了");
        });

        //anyOf / allOf
        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(either1, either2);
        System.out.println("anyOf Result:" + anyOf.get());

        CompletableFuture<Void> allOf = CompletableFuture.allOf(either1, either2);
        System.out.println("allOf Result:" + allOf.get());


        //4.结果处理：完成 / 异常 - whenComplete \ handle \ exceptionally
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            Integer a = 2;
            int res = 10 / a;
            return res;
        });
        future.whenComplete((integer, throwable) -> {
            //运行正常返回5，当a=0时，运行异常返回null
            System.out.println("whenComplete: " + integer);
        }).exceptionally(throwable -> {
            System.out.println("执行失败 " + throwable.getMessage());
            return 0;
        }).handle((integer, throwable) -> {
            System.out.println("相当于finally方法");
            return 0;
        });
        //System.out.println(future.get());

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        t1();
    }


}
