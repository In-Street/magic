package com.magic.time.service.business_development_100.httpinvoke_05;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Cheng Yufei
 * @create 2021-12-28 10:10 PM
 **/
@FeignClient(name = "clientsdk")
public interface Client {

    @PostMapping("/clientReadTimeout/server")
    void server();
}
