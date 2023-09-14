package top.flobby.coupon.calculation.template.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.coupon.calculation.template.AbstractRuleTemplate;
import top.flobby.coupon.calculation.template.RuleTemplate;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 折扣模板
 * @create : 2023-09-08 17:17
 **/

@Slf4j
@Component
public class DiscountTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        // 计算使用优惠券之后的价格
        Long newPrice = convertToDecimal(shopTotalAmount * (quota.doubleValue() / 100));
        log.debug("原始价格 = {}， 新价格 = {}", orderTotalAmount, newPrice);
        return newPrice;
    }
}
