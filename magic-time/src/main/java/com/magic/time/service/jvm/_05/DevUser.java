package com.magic.time.service.jvm._05;

import com.magic.time.service.jvm._04.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 *
 * @author Cheng Yufei
 * @create 2023-01-15 17:55
 **/
@Getter
@Setter
public class DevUser extends User<ArrayList> {

    private Integer age;

    private String username;

    public DevUser(String username) {
        this(username, 1);
    }

    public DevUser(String username, Integer age) {
        super(username);
        this.age = age;
    }

    public DevUser() {
        super("默认username");
    }

    /**
     * 子类隐藏父类的静态方法
     *
     * @return
     */
    public static String getU() {
        return "子类DevUser: getU";
    }

    @Override
    protected String getU2(ArrayList arrayList) throws IndexOutOfBoundsException {
        return "子类DevUser: getU2";
    }

    protected String getU2(Integer a) throws IndexOutOfBoundsException {
        return "子类DevUser: getU2";
    }

    protected Integer getU2(double a) throws IndexOutOfBoundsException {
        return 1;
    }

}
