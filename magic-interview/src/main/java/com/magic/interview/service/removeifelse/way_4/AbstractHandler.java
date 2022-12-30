package com.magic.interview.service.removeifelse.way_4;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2022-12-29 16:15
 **/
public abstract class AbstractHandler {

    /**
     *  各个子类实现具体处理流程
     * @param userData
     * @return
     */
    public abstract String handler(UserData userData);

    protected AbstractHandler nextHandler;

    protected String execute(UserData userData) {
        if (Objects.isNull(nextHandler)) {
            return "success";
        }
        return nextHandler.handler(userData);
    }



}
