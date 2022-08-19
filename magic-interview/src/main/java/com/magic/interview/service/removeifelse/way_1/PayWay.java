package com.magic.interview.service.removeifelse.way_1;

import java.lang.annotation.*;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:02
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PayWay {

    String code();
    String desc();
}
