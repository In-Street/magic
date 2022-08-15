package com.magic.interview.service.lambda_exception;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Cheng Yufei
 * @create 2019-10-31 16:32
 **/
public interface Handle {

    static <T, R> Function<T, R> handle(CheckFunction<T, R> checkFunction) {

        return t -> {
            try {
                return checkFunction.apply(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    static <T> Consumer<T> handleConsumer(CheckConsumer<T> checkConsumer) {

        return t -> {
            try {
                checkConsumer.accept(t);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    static <T> Supplier<T> handleSupplier(CheckSupplier<T> checkSupplier) {
        return () -> {
            try {
               return checkSupplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}


