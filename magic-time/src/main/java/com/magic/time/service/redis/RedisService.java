package com.magic.time.service.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2025-02-05 18:40
 **/
@Service
@Slf4j
public class RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String test1(){
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        Boolean result = opsForValue.setIfAbsent("string-a", "Aa", 20, TimeUnit.SECONDS);
        Boolean result2 = opsForValue.setIfPresent("string-b", "Bb", 20, TimeUnit.SECONDS);

        return "succsess";
    }
}
