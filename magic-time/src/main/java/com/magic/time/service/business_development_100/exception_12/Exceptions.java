package com.magic.time.service.business_development_100.exception_12;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Cheng Yufei
 * @create 2022-02-06 4:03 PM
 **/
public class Exceptions {

    public static BusinessException commonException = new BusinessException(1000, "订单异常");

    public static BusinessException commonException2() {
        return new BusinessException(1000, "订单异常");
    }


    @Getter
    @Setter
    @AllArgsConstructor
    static class BusinessException extends RuntimeException{
        private Integer code;
        private String msg;

    }
}
