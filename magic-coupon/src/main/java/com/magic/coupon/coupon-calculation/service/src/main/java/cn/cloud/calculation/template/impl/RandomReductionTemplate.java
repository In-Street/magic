package cn.cloud.calculation.template.impl;

import cn.cloud.calculation.template.AbstractRuleTemplate;
import cn.cloud.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 随机减 优惠券，计算规则
 * @author Cheng Yufei
 * @create 2023-03-23 18:21
 **/
@Slf4j
@Component
public class RandomReductionTemplate extends AbstractRuleTemplate implements RuleTemplate {

    @Override
    protected Long calculateNewPrice(Long totalAmount, Long shopAmount, Long quota) {
        // 计算使用优惠券之后的价格
        Long maxBenefit = Math.min(shopAmount, quota);
        int reductionAmount = new Random().nextInt(maxBenefit.intValue());
        Long newCost = totalAmount - reductionAmount;

        log.debug("original price={}, new price={}", totalAmount, newCost );
        return newCost;
    }
}
