package com.magic.coupon.coupon;

import cn.cloud.template.api.beans.CouponInfo;
import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 *
 * @author Cheng Yufei
 * @create 2023-03-23 17:32
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    /**
     * 订单商品列表
     */
    @NotEmpty
    private List<Product> products;

    private Long couponId;

    /**
     * 订单最终价格
     */
    private long cost;

    // 目前只支持单张优惠券
    // 但是为了以后的扩展考虑，你可以添加多个优惠券的计算逻辑
    private List<CouponInfo> couponInfos;

    @NotNull
    private Long userId;
}
