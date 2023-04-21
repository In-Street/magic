package com.magic.time.service.jvm._04;

import lombok.Getter;
import lombok.Setter;

import java.util.AbstractList;
import java.util.List;
import java.util.zip.Deflater;

/**
 *
 * @author Cheng Yufei
 * @create 2023-01-15 17:54
 **/
@Getter
@Setter
public class User<T extends AbstractList> {

    private String username;

    public User(String username) {
        this.username = username;
    }

    protected String sx = "abc";
    public static String getU() {
        return "父类：getU";
    }

     protected Object getU2(T list) throws Exception {
        return "父类User: getU2";
    }

     String getU3(Integer a) throws IndexOutOfBoundsException {
        return "getU3";
    }

    protected Integer getU4(String a) throws IndexOutOfBoundsException {
        return 1;
    }
}
