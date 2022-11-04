package com.magic.interview.service.proxy;

import com.magic.dao.model.User;

/**
 *
 * @author Cheng Yufei
 * @create 2022-10-14 15:33
 **/
public class UInterfaceImpl implements UInterface{
    @Override
    public void check(User user) {
        System.out.println(user);
    }
}
