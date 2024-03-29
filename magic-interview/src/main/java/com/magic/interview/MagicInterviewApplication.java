package com.magic.interview;

import cn.anony.boot.annotation.EnableSms;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.magic.interview.Config.GsonIgnore;
import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Objects;

/**
 * @author
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.magic.interview", "com.magic.dao"}, exclude = {RabbitAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableSms
@EnableAsync
//获取配置文件加密盐值
//@PropertySource(value = {"file:D:/encrypt.properties"})
@PropertySource(value = {"file:/Users/chengyufei/Downloads/project/self/encrypt.properties"})
//leaf 分布式ID
@EnableLeafServer
public class MagicInterviewApplication {

    @Value("${jasypt.encryptor.password}")
    private String saltValue;

    public static void main(String[] args) {

        SpringApplication.run(MagicInterviewApplication.class, args);
        //new SpringApplicationBuilder(MagicInterviewApplication.class).run(args);
    }

    /**
     *设置jasypt加/解密类
     * @return
     */
    @Bean
    public BasicTextEncryptor basicTextEncryptor() {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(saltValue);
        return encryptor;
    }

    @Bean
    public RedisTemplate getRedisTemplate(RedisTemplate redisTemplate) {

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //会对value进行设置
        //redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name = "gson")
    public Gson initGson() {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return Objects.nonNull(f.getAnnotation(GsonIgnore.class));
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        }).create();
    }

    @Bean(name = "objectMapper")
    public ObjectMapper initObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        //反序列化时有不存在的属性时忽略
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        //序列化时忽略null 值的字段
        //objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }
}
