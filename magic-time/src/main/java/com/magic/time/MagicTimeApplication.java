package com.magic.time;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.time"}, exclude = {RabbitAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
public class MagicTimeApplication {



    public static void main(String[] args) {
        try {
        SpringApplication.run(MagicTimeApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //@Bean(name = "objectMapper")
    public ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //反序列化时有不存在的属性时忽略
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //序列化时忽略null 值的字段
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
