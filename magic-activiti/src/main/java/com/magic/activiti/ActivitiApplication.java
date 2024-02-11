package com.magic.activiti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Cheng Yufei
 * @create 2024-02-01 15:39
 **/
@SpringBootApplication(scanBasePackages = {"com.magic.activiti", "com.magic.dao"})
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
    }
}
