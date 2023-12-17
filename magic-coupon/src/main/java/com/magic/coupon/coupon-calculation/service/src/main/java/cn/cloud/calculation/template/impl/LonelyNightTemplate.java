package cn.cloud.calculation.template.impl;

import cn.cloud.calculation.template.AbstractRuleTemplate;
import cn.cloud.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * 午夜10点～凌晨2点之间下单，优惠金额翻倍
 * @author Cheng Yufei
 * @create 2023-03-23 18:23
 **/
@Slf4j
@Component
public class LonelyNightTemplate extends AbstractRuleTemplate implements RuleTemplate {


    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        if (hourOfDay >= 23 || hourOfDay < 2) {
            quota *= 2;
        }

        Long benefitAmount = shopTotalAmount < quota ? shopTotalAmount : quota;
        return orderTotalAmount - benefitAmount;
    }
}
