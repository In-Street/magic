package com.magic.interview.service.valueandel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-24 10:52
 **/
@Configuration
@ConfigurationProperties(prefix = "test")
public class ValueConfig {
    @Getter
    @Setter
    private List<String> list2;
}
