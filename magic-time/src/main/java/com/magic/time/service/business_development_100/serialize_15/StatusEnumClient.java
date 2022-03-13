package com.magic.time.service.business_development_100.serialize_15;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.Getter;

/**
 *
 * @author Cheng Yufei
 * @create 2022-03-13 12:03 AM
 **/
@Getter
public enum StatusEnumClient {

    CREATED(1, "已创建"),
    PAID(2, "已支付"),
    DELIVERED(3, "已送到"),
    FINISHED(4, "已完成"),
    @JsonEnumDefaultValue
    UNKNOWN(-1,"未知"),
    ;
    private final int status;
    private final String desc;

    StatusEnumClient(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
