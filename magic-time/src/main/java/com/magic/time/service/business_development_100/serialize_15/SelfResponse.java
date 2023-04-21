package com.magic.time.service.business_development_100.serialize_15;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author Cheng Yufei
 * @create 2022-03-12 11:27 PM
 **/
@Data
public class SelfResponse {

    private int code;
    private boolean success;

    public SelfResponse(int code, boolean success) {
        this.code = code;
        this.success = success;
    }

    //@JsonCreator
    public SelfResponse(/*@JsonProperty*/ int code) {
        this.code = code;
        success = code == 1000 ? true : false;
    }
}
