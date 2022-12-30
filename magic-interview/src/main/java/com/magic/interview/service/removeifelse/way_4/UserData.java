package com.magic.interview.service.removeifelse.way_4;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 *
 * @author Cheng Yufei
 * @create 2021-07-13 3:58 下午
 **/
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserData {
    String uid;
    Integer roleType;
    long time;
    String token;
    String ip;

    @Test
    public void handler() {
        Build build = new Build();
        build.add(new NullCheckHandler())
                .add(new TokenCheckHandler())
                .add(new TimeCheckHandler());
        //组装处理链： NullCheck -> TokenCheck -> TimeCheck
        AbstractHandler handler = build.build();

        UserData userData = new UserData();
        userData.setUid("AA");
        userData.setToken("ABC");
        userData.setTime(DateUtils.addMinutes(new Date(),-3).getTime());

        String result = handler.handler(userData);
        System.out.println(result);
    }
}
