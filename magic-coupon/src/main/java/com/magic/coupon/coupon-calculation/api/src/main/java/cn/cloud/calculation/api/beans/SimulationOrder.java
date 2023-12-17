package com.magic.coupon.coupon;

import cn.cloud.template.api.beans.CouponInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 价格试算，模拟计算每张优惠券可以扣减的金额，进而选择最优惠的券来使用
 * @author Cheng Yufei
 * @create 2023-03-23 17:55
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationOrder {

    @NotEmpty
    private List<Product> products;

    @NotEmpty
    private List<Long> couponIDs;

    private List<CouponInfo> couponInfos;

    @NotNull
    private Long userId;
}
