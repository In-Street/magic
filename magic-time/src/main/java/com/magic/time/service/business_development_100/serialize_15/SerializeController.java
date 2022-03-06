package com.magic.time.service.business_development_100.serialize_15;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magic.time.dao.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-13 6:25 PM
 **/
@RestController
@RequestMapping("/serialize")
@Slf4j
public class SerializeController {

    /*@Autowired
    private RedisTemplate redisTemplate;*/
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, UserInfo> userInfoRedisTemplate;

    /**
     * 未自定义RedisTemplate的Key、Value的序列化方式时
     * @throws JsonProcessingException
     */
    @GetMapping("/case1")
    public void case1() throws JsonProcessingException {
        //redisTemplate.opsForValue().set("redisTemplate",new UserInfo(1,"退后"));
        stringRedisTemplate.opsForValue().set("stringRedisTemplate", objectMapper.writeValueAsString(new UserInfo(2, "借口")));

    }

    /**
     * 未自定义RedisTemplate的Key、Value的序列化方式时
     * @throws JsonProcessingException
     */
    @GetMapping("/get")
    public void get() {
        //log.info("redisTemplate: {}",redisTemplate.opsForValue().get("stringRedisTemplate"));
        log.info("stringRedisTemplate: {}", stringRedisTemplate.opsForValue().get("redisTemplate"));
        log.info("stringRedisTemplate-case2: {}", stringRedisTemplate.opsForValue().get("case3"));

        //Jackson2JsonRedisSerializer 未指定ObjectMapper时，输出类型为LinkedHashMap
        Object case2 = userInfoRedisTemplate.opsForValue().get("case3");
        log.info("userInfoRedisTemplate: {}，{}", case2,case2.getClass());
    }

    @GetMapping("/case2")
    public void case2() {
        UserInfo userInfo = new UserInfo(3, "不能说的秘密");
        userInfoRedisTemplate.opsForValue().set("case3", userInfo);
    }

    @Bean(name = "redisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        //objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

/**
 * [
 *   "com.magic.time.dao.model.UserInfo",
 *   {
 *     "id": 3,
 *     "username": "不能说的秘密",
 *     "avatar": null,
 *     "address": null,
 *     "createTime": null,
 *     "updateTime": null
 *   }
 * ]
 */