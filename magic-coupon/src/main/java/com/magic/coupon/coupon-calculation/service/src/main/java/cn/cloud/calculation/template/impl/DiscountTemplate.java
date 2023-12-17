package cn.cloud.calculation.template.impl;

import cn.cloud.calculation.template.AbstractRuleTemplate;
import cn.cloud.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 打折优惠券
 * @author Cheng Yufei
 * @create 2023-03-23 18:13
 **/
@Slf4j
@Component
public class DiscountTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        long newPrice = convertToDecimal(shopTotalAmount * (quota.doubleValue() / 100));
        log.debug("打折优惠券,original price={}, new price={}", orderTotalAmount, newPrice);
        return newPrice;
    }
}
