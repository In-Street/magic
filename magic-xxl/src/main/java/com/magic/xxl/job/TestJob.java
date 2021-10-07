package com.magic.xxl.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 *
 * @author Cheng Yufei
 * @create 2021-05-27 10:30 上午
 **/
@Component
public class TestJob {

    @XxlJob(value = "simpleJobHandle")
    public void simple() throws InterruptedException {
        XxlJobHelper.log("Hello World");
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            Thread.sleep(2000);
        }
    }


}
