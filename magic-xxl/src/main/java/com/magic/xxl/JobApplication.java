package com.magic.xxl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * @author
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.xxl"}, exclude = {RabbitAutoConfiguration.class})
public class JobApplication {


    public static void main(String[] args) {

        SpringApplication.run(JobApplication.class, args);
    }
}
