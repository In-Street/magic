package com.magic.interview.service.removeifelse.way_4;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Cheng Yufei
 * @create 2022-12-29 17:03
 **/
public class NullCheckHandler extends AbstractHandler{
    @Override
    public String handler(UserData userData) {
        if (StringUtils.isAnyBlank(userData.getUid(),userData.getToken())) {
            return "字段为空，校验失败";
        }

        //执行下一个处理器
        return execute(userData);
    }
}
