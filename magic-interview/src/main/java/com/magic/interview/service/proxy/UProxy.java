package com.magic.interview.service.proxy;

import com.magic.dao.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 *
 * @author Cheng Yufei
 * @create 2022-10-14 15:34
 **/
@NoArgsConstructor
public class UProxy implements InvocationHandler {

    @Getter @Setter
    private Object object;

    public UProxy(Object object) {
        super();
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        User user = (User) args[0];
        String username = user.getUsername();
        if (username.length() < 6) {
            throw new Exception("长度不满足");
        }

        return method.invoke(object, args);
    }
}
