package com.magic.interview.service.removeifelse.way_2;

import org.springframework.stereotype.Service;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:01
 **/
@Service
public class AliPayWay2 implements IPayWay2 {


    @Override
    public boolean support(String code) {
        return "ali".equals(code);
    }

    @Override
    public String pay() {
        return "AliPay";
    }
}
