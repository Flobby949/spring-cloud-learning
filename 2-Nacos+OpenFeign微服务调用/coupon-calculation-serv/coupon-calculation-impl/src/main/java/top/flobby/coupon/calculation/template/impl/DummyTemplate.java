package top.flobby.coupon.calculation.template.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.template.AbstractRuleTemplate;
import top.flobby.coupon.calculation.template.RuleTemplate;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 空实现
 * @create : 2023-09-08 17:18
 **/

@Slf4j
@Component
public class DummyTemplate extends AbstractRuleTemplate implements RuleTemplate {

    @Override
    public ShoppingCart calculate(ShoppingCart order) {
        // 获取订单总价
        long totalPrice = getTotalPrice(order.getProducts());
        order.setCost(totalPrice);
        return order;
    }

    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        return orderTotalAmount;
    }
}
