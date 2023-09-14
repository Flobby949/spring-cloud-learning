package top.flobby.coupon.calculation.template.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.coupon.calculation.template.AbstractRuleTemplate;
import top.flobby.coupon.calculation.template.RuleTemplate;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 满减模板
 * @create : 2023-09-08 17:18
 **/

@Slf4j
@Component
public class MoneyOffTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    protected Long calculateNewPrice(Long totalAmount, Long shopAmount, Long quota) {
        // benefitAmount是扣减的价格
        // 如果当前门店的商品总价<quota，那么最多只能扣减shopAmount的钱数
        Long benefitAmount = shopAmount < quota ? shopAmount : quota;
        return totalAmount - benefitAmount;
    }
}
