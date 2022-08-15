package com.magic.interview.service.lambda_exception;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-12 14:43
 **/
@FunctionalInterface
public interface CheckConsumer<T> {

    void accept(T t) throws Exception;
}
