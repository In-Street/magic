package com.magic.interview.service.removeifelse.way_1;

import org.springframework.stereotype.Service;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:01
 **/
@Service
@PayWay(code = "wx", desc = "微信支付")
public class WxPay implements IPay {

    @Override
    public String pay() {
        return "WxPay";
    }
}
