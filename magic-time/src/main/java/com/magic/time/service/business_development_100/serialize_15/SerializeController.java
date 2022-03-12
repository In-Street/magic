package com.magic.time.service.business_development_100.serialize_15;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.magic.time.dao.model.OrgInfo;
import com.magic.time.dao.model.UserInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.*;

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
    @Qualifier("objectRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("objectRedisTemplate2")
    private RedisTemplate redisTemplate2;

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
    @GetMapping("/get/{key}")
    public void get(@PathVariable String key) {
        //log.info("redisTemplate: {}",redisTemplate.opsForValue().get("stringRedisTemplate"));
        log.info("stringRedisTemplate: {}", stringRedisTemplate.opsForValue().get("redisTemplate"));
        log.info("stringRedisTemplate-case2: {}", stringRedisTemplate.opsForValue().get(key));

        //Jackson2JsonRedisSerializer 未指定ObjectMapper时，输出类型为LinkedHashMap
        Object case2 = redisTemplate.opsForValue().get(key);
        log.info("userInfoRedisTemplate: {}，{}", case2, case2.getClass());
    }

    /**
     * 定义RedisTemplate的序列化方式
     */
    @GetMapping("/set")
    public void case2() {
        ValueOperations opsForValue = redisTemplate.opsForValue();
        UserInfo userInfo = new UserInfo(5, "不能说的秘密");
        //opsForValue.set("user5", userInfo);

        OrgInfo orgInfo = new OrgInfo();
        orgInfo.setOrgId("AAAAA");
        orgInfo.setOrgName("冬天的秘密");
        //opsForValue.set("org1",orgInfo);


        OrgInfo orgInfo2 = new OrgInfo();
        orgInfo2.setOrgId("BBBB");
        orgInfo2.setOrgName("一路向北");
        redisTemplate2.opsForValue().set("org2", orgInfo2);
    }

    /**
     * 默认ObjectMapper 序列化枚举为字符串''BLUE"
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/test")
    public String test() throws JsonProcessingException {
        String s = objectMapper.writeValueAsString(Color.BLUE);
        log.info(">>序列化枚举:{}", s);
        return s;
    }

    /**
     * 自定义ObjectMapper 需要设定 FAIL_ON_UNKNOWN_PROPERTIES
     * @param orgInfo
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/test2")
    public OrgInfo test(@RequestBody OrgInfo orgInfo) throws JsonProcessingException {
        return orgInfo;
    }

    @GetMapping("/res/{code}")
    public SelfResponse res(@PathVariable int code) throws JsonProcessingException {
        //return new SelfResponse(code);
        return objectMapper.readValue("{\"code\":"+code+"}", SelfResponse.class);
    }

    @PostMapping("/res")
    public SelfResponse res(@RequestBody SelfResponse selfResponse)  {
        log.info("反序列化：{}", selfResponse);
        return selfResponse;
    }


    //@Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return build -> build.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
    }

    @Bean(name = "objectRedisTemplate")
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        //objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean(name = "objectRedisTemplate2")
    public <T> RedisTemplate<String, T> redisTemplate2(RedisConnectionFactory factory) {
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Getter
    public enum Color {

        RED(1, "红色"),
        BLUE(2, "蓝色");

        private Integer code;
        private String name;

        Color(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Color get(Integer code) {
            Color[] values = Color.values();
            for (Color value : values) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
            return null;
        }
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