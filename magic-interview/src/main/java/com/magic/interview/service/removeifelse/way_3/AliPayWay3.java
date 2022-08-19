package com.magic.interview.service.removeifelse.way_3;

import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:01
 **/
@Service
public class AliPayWay3 extends PayHandler {

    @Override
    public String pay(String code) {
        if ("ali".equals(code)) {
            return "AliPay";
        }
        PayHandler next = getNext();
        if (Objects.isNull(next)) {
            return "未找到";
        }
        return next.pay(code);
    }
}
