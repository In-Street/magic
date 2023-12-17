package cn.cloud.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Cheng Yufei
 * @create 2023-03-23 17:29
 **/

@SpringBootApplication(scanBasePackages = "cn.cloud.calculation")
public class CalculationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculationServiceApplication.class, args);
    }
}
