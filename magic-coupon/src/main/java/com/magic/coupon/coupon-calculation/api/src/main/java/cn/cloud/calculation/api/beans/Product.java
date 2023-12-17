package com.magic.coupon.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Cheng Yufei
 * @create 2023-03-23 17:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    // 你可以试着搭建一个商品中心，用来存储商品信息，逐步完善这个应用
    private Long productId;

    /**
     * 商品的价格
     */
    private long price;

    /**
     * 商品在购物车里的数量
     */
    private Integer count;

    /**
     * 商品销售的门店
     */
    private Long shopId;

}
