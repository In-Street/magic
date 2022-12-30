package com.magic.interview.service.removeifelse.way_4;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 *
 * @author Cheng Yufei
 * @create 2022-12-29 17:09
 **/
public class TimeCheckHandler extends AbstractHandler{
    @Override
    public String handler(UserData userData) {
        long time = userData.getTime();
        if (new Date().before(new Date(time))) {
            return "过期时间，失败";
        }
        //执行下一个处理器
        return execute(userData);
    }
}
