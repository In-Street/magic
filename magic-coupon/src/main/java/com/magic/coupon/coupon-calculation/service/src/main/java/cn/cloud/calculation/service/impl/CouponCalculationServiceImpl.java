package cn.cloud.calculation.service.impl;

import cn.cloud.calculation.api.beans.ShoppingCart;
import cn.cloud.calculation.api.beans.SimulationOrder;
import cn.cloud.calculation.api.beans.SimulationResponse;
import cn.cloud.calculation.service.CouponCalculationService;
import cn.cloud.calculation.template.CouponTemplateFactory;
import cn.cloud.calculation.template.RuleTemplate;
import cn.cloud.template.api.beans.CouponInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Cheng Yufei
 * @create 2023-03-23 18:29
 **/
@Slf4j
@Service
public class CouponCalculationServiceImpl implements CouponCalculationService {

    @Autowired
    private CouponTemplateFactory couponProcessorFactory;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 优惠券结算
     * @param cart
     * @return
     */
    // 这里通过Factory类决定使用哪个底层Rule，底层规则对上层透明
    @Override
    public ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart cart) throws JsonProcessingException {
        log.info("calculate order price: {}", objectMapper.writeValueAsString(cart));
        RuleTemplate ruleTemplate = couponProcessorFactory.getTemplate(cart);
        return ruleTemplate.calculate(cart);
    }


    /**
     * 订单价格试算，帮助用户在下单前了解每个优惠券在使用后订单的价格。
     *  提示用户最省钱的优惠券
     *
     * @param order
     * @return
     */
    @Override
    public SimulationResponse simulateOrder(@RequestBody SimulationOrder order) {
        SimulationResponse response = new SimulationResponse();
        Long minOrderPrice = Long.MAX_VALUE;

        // 计算每一个优惠券的订单价格
        for (CouponInfo coupon : order.getCouponInfos()) {
            ShoppingCart cart = new ShoppingCart();
            cart.setProducts(order.getProducts());
            cart.setCouponInfos(Lists.newArrayList(coupon));
            cart = couponProcessorFactory.getTemplate(cart).calculate(cart);

            Long couponId = coupon.getId();
            Long orderPrice = cart.getCost();

            // 设置当前优惠券对应的订单价格
            response.getCouponToOrderPrice().put(couponId, orderPrice);

            // 比较订单价格，设置当前最优优惠券的ID
            if (minOrderPrice > orderPrice) {
                response.setBestCouponId(coupon.getId());
                minOrderPrice = orderPrice;
            }
        }
        return response;
    }

}