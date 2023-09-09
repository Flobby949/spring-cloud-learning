package top.flobby.coupon.calculation.template;

import top.flobby.coupon.calculation.api.beans.ShoppingCart;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 规则模板
 * @create : 2023-09-08 17:15
 **/

public interface RuleTemplate {
    /**
     * 计算优惠券
     *
     * @param settlement 入参
     * @return {@link ShoppingCart}
     */
    ShoppingCart calculate(ShoppingCart settlement);
}
