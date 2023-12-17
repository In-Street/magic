package cn.cloud.calculation.template.impl;

import cn.cloud.calculation.template.AbstractRuleTemplate;
import cn.cloud.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 满减优惠券计算规则
 * @author Cheng Yufei
 * @create 2023-03-23 18:17
 **/
@Component
@Slf4j
public class MoneyOffTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {

        // benefitAmount是扣减的价格
        // 如果当前门店的商品总价<quota，那么最多只能扣减shopAmount的钱数
        Long benefitAmount = shopTotalAmount < quota ? shopTotalAmount : quota;
        return orderTotalAmount - benefitAmount;
    }
}
