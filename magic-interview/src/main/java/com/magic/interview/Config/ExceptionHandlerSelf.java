package com.magic.interview.Config;

import com.magic.base.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-05 15:00
 **/
//@RestControllerAdvice
@Slf4j
public class ExceptionHandlerSelf {

    @ExceptionHandler(value = {RuntimeException.class})
    public Result handle() {
        return Result.fail(100, "失败");
    }
}
