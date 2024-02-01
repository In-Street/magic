package com.magic.day;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Cheng Yufei
 * @create 2024-02-01 15:39
 **/
@SpringBootApplication(scanBasePackages = {"com.magic.day", "com.magic.dao"})
public class DayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DayApplication.class, args);
    }
}
