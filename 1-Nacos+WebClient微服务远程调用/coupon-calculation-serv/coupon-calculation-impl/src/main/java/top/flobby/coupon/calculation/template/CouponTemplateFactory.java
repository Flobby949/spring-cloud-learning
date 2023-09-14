package top.flobby.coupon.calculation.template;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.template.impl.*;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;
import top.flobby.coupon.template.api.enums.CouponType;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 优惠券模板工厂
 * @create : 2023-09-08 17:15
 **/

@Slf4j
@Component
public class CouponTemplateFactory {
    @Resource
    private MoneyOffTemplate moneyOffTemplate;
    @Resource
    private DiscountTemplate discountTemplate;

    @Resource
    private RandomReductionTemplate randomReductionTemplate;

    @Resource
    private NightTemplate nightTemplate;

    @Resource
    private DummyTemplate dummyTemplate;

    public RuleTemplate getTemplate(ShoppingCart order) {
        // 不使用优惠券，获取优惠券类别
        if (!CollectionUtils.isEmpty(order.getCouponInfos())) {
            // 目前每个订单只支持单张优惠券
            CouponTemplateInfo template = order.getCouponInfos().get(0).getTemplate();
            CouponType category = CouponType.convert(template.getType());
            log.info("当前使用优惠券类型：{}", category.getDescription());
            return switch (category) {
                // 订单满减券
                case MONEY_OFF -> moneyOffTemplate;
                // 随机立减券
                case RANDOM_DISCOUNT -> randomReductionTemplate;
                // 午夜下单优惠翻倍
                case NIGHT_MONEY_OFF -> nightTemplate;
                // 打折券
                case DISCOUNT -> discountTemplate;
                // 未知类型的券模板
                default -> dummyTemplate;
            };
        } else {
            // dummy直接返回原价
            return dummyTemplate;
        }
    }
}
