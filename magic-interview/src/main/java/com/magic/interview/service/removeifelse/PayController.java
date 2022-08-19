package com.magic.interview.service.removeifelse;

import com.magic.interview.service.removeifelse.way_1.PayService;
import com.magic.interview.service.removeifelse.way_2.PayServiceWay2;
import com.magic.interview.service.removeifelse.way_2.PayServiceWay2_2;
import com.magic.interview.service.removeifelse.way_3.PayServiceWay3;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-15 17:17
 **/
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;
    @Autowired
    private PayServiceWay2 payServiceWay2;
    @Autowired
    private PayServiceWay2_2 payServiceWay2_2;
    @Autowired
    private PayServiceWay3 payServiceWay3;

    @GetMapping("/way1")
    public String p1(@RequestParam String code) {
        return payService.pay(code);
    }

    @GetMapping("/way2")
    public String p2(@RequestParam String code) {
        return payServiceWay2_2.pay(code);
    }

    @GetMapping("/way3")
    public String p3(@RequestParam String code) {
        return payServiceWay3.pay(code);
    }
}
