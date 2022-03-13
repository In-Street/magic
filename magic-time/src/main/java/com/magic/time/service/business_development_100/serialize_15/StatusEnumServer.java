package com.magic.time.service.business_development_100.serialize_15;

import lombok.Getter;

/**
 *
 * @author Cheng Yufei
 * @create 2022-03-13 12:03 AM
 **/
@Getter
public enum StatusEnumServer {
    CREATED(1, "已创建"),
    PAID(2, "已支付"),
    DELIVERED(3, "已送到"),

    CANCELED(5,"已取消") ,
    FINISHED(4, "已完成"),


    ;


    private final int status;
    private final String desc;

    StatusEnumServer(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
