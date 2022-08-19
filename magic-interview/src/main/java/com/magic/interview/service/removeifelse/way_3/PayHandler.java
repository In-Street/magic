package com.magic.interview.service.removeifelse.way_3;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Cheng Yufei
 * @create 2022-08-19 14:28
 **/
@Getter
@Setter
public abstract class PayHandler {

    private PayHandler next;

    public abstract String pay(String code);
}
