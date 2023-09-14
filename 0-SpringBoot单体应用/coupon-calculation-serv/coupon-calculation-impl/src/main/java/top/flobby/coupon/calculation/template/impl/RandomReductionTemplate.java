package top.flobby.coupon.calculation.template.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.coupon.calculation.template.AbstractRuleTemplate;
import top.flobby.coupon.calculation.template.RuleTemplate;

import java.util.Random;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 随机模板
 * @create : 2023-09-08 17:19
 **/

@Slf4j
@Component
public class RandomReductionTemplate extends AbstractRuleTemplate implements RuleTemplate {
    @Override
    protected Long calculateNewPrice(Long totalAmount, Long shopAmount, Long quota) {
        long maxBenefit = Math.min(shopAmount, quota);
        int reductionAmount = new Random().nextInt((int) maxBenefit);
        Long newPrice = totalAmount - reductionAmount;
        log.debug("原始价格 = {}， 新价格 = {}", totalAmount, newPrice);
        return newPrice;
    }
}
