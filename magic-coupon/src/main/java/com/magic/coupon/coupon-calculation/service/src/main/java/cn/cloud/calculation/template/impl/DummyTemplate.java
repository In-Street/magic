package cn.cloud.calculation.template.impl;

import cn.cloud.calculation.api.beans.ShoppingCart;
import cn.cloud.calculation.template.AbstractRuleTemplate;
import cn.cloud.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 空实现
 * @author Cheng Yufei
 * @create 2023-03-23 18:41
 **/
@Slf4j
@Component
public class DummyTemplate extends AbstractRuleTemplate implements RuleTemplate {

    @Override
    public ShoppingCart calculate(ShoppingCart order) {
        // 获取订单总价
        Long orderTotalAmount = getTotalPrice(order.getProducts());

        order.setCost(orderTotalAmount);
        return order;
    }


    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        return orderTotalAmount;
    }
}
