package com.magic.interview.service.removeifelse.way_1;

import org.springframework.stereotype.Service;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:01
 **/
@Service
@PayWay(code = "ali", desc = "阿里支付")
public class AliPay implements IPay{

    @Override
    public String pay() {
        return "AliPay";
    }
}
