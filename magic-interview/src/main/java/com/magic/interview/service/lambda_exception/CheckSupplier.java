package com.magic.interview.service.lambda_exception;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 10:45
 **/
@FunctionalInterface
public interface CheckSupplier<T> {
    T get() throws Exception;
}
