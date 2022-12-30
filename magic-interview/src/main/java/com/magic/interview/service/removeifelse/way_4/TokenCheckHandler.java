package com.magic.interview.service.removeifelse.way_4;

/**
 *
 * @author Cheng Yufei
 * @create 2022-12-29 17:05
 **/
public class TokenCheckHandler extends AbstractHandler{
    @Override
    public String handler(UserData userData) {
        String token = userData.getToken();
        if (!token.equals("ABC")) {
            return "token校验失败";
        }
        //执行下一个处理器
        return execute(userData);
    }
}
