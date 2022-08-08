package com.magic.interview.service.jackson.customDeserial;

import lombok.Data;


/**
 *
 * @author Cheng Yufei
 * @create 2022-08-05 15:52
 **/
@Data
public class UserTest {


    @T1( cutLen = 2)
    private String username;

    @T1(cutLen = 3)
    private String address;

    private String aa;
}
