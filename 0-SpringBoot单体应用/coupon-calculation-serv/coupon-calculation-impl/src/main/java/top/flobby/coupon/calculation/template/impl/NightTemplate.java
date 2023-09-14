package top.flobby.coupon.calculation.template.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.coupon.calculation.template.AbstractRuleTemplate;
import top.flobby.coupon.calculation.template.RuleTemplate;

import java.util.Calendar;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 夜间折扣模板
 * @create : 2023-09-08 17:18
 **/

@Slf4j
@Component
public class NightTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        // 晚上十一点到次日凌晨两点间下单，优惠金额翻倍
        if (hourOfDay == 23 || hourOfDay < 2) {
            quota *= 2;
        }
        Long benefitAmount = Math.min(shopTotalAmount, quota);
        return orderTotalAmount - benefitAmount;
    }
}
