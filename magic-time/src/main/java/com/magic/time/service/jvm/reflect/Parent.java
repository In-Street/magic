package com.magic.time.service.jvm.reflect;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Cheng Yufei
 * @create 2025-01-31 11:16
 **/
public class Parent {

    private int p1;
    public  int p2;

    protected String parentMethodProtected(String param){
        return param;
    }

    public String parentMethodPublic(String param){

        new Exception(">>>"+param).printStackTrace();
        return param;
    }

     Integer parentMethod2(){
        return 1;
    }

    Integer parentMethodPrivate(){
        return 1;
    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }
}
