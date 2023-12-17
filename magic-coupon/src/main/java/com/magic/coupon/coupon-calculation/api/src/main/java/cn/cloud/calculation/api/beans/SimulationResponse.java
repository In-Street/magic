package com.magic.coupon.coupon;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 价格试算的返回结果
 * @author Cheng Yufei
 * @create 2023-03-23 17:57
 **/
@Data
@NoArgsConstructor
public class SimulationResponse {

    /**
     * 最省钱的coupon
     */
    private Long bestCouponId;

    /**
     * 每一个coupon对应的order价格
     */
    private Map<Long, Long> couponToOrderPrice = Maps.newHashMap();

}
