package com.magic.interview.service.jackson.customDeserial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-05 15:51
 **/
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/t1")
    public UserTest t1() throws JsonProcessingException {
        String u = "{\"username\":\"BBBBB\",\"address\":\"北京市朝阳区\",\"aa\":\"AAAAAAAA\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        UserTest userTest = objectMapper.readValue(u, UserTest.class);
        return userTest;
    }
}
