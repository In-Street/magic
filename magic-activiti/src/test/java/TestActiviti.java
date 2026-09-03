import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.db.DbIdGenerator;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Cheng Yufei
 * @create 2024-05-04 18:48
 **/
public class TestActiviti {

    @Test
    public void idGenerator() {
        String areaId = "14";
        String right_areaId = StringUtils.right(areaId, 2);
        System.out.println(right_areaId);

        String center = StringUtils.substring("140103", 2, 4);
        System.out.println(center);
    }

    @Test
    public void t2() throws ExecutionException, InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(2);
        System.out.println("主线程开始");
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("开始计算");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("结束计算");
            return "123";
        }, pool);

        // String result = future.get();
        // System.out.println("同步获取计算结果: " + result);
        future.thenAcceptAsync(result->System.out.println("异步获取计算结果: " + result), pool);


        System.out.println("主线程结束");
    }
}
