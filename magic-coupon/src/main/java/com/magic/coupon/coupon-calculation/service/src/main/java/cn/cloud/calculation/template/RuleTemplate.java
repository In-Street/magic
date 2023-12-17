package cn.cloud.calculation.template;

import cn.cloud.calculation.api.beans.ShoppingCart;

/**
 * 顶级接口，提供计算方法
 * @author Cheng Yufei
 * @create 2023-03-23 18:00
 **/
public interface RuleTemplate {

    ShoppingCart calculate(ShoppingCart shoppingCart);
}
