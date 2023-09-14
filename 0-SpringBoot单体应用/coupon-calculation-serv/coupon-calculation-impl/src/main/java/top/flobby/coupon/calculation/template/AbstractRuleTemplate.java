package top.flobby.coupon.calculation.template;

import lombok.extern.slf4j.Slf4j;
import top.flobby.coupon.calculation.api.beans.Product;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 定义通用的计算逻辑
 * @create : 2023-09-08 17:16
 **/

@Slf4j
public abstract class AbstractRuleTemplate implements RuleTemplate {
    @Override
    public ShoppingCart calculate(ShoppingCart order) {
        // 获取订单总价
        Long orderTotalAmount = getTotalPrice(order.getProducts());
        // 获取以shopId为维度的价格统计
        Map<Long, Long> sumAmount = this.getTotalPriceGroupByShop(order.getProducts());

        // 以下规则只做单个优惠券的计算
        CouponTemplateInfo template = order.getCouponInfos().get(0).getTemplate();
        // 最低消费限制
        Long threshold = template.getRule().getDiscount().getThreshold();
        // 优惠金额或者打折比例
        Long quota = template.getRule().getDiscount().getQuota();
        // 优惠券适用门店id，如为空则无门槛
        Long shopId = template.getShopId();

        // 如优惠券无门槛，shopTotalAmount = orderTotalAmount
        // 如果指定shopId，则shopTotalAmount = 对应门店下商品的总价
        Long shopTotalAmount = (shopId == null) ? orderTotalAmount : sumAmount.get(shopId);

        // 如果优惠券不符合适用标准，则原价
        if (shopTotalAmount == null || shopTotalAmount < threshold) {
            log.warn("商品金额不满足优惠券适用标准！");
            order.setCost(orderTotalAmount);
            order.setCouponInfos(Collections.emptyList());
            return order;
        }

        // 子类中计算新的价格
        // 订单价格不能小于最低价格
        long newCost = Math.max(minCost(), calculateNewPrice(orderTotalAmount, shopTotalAmount, quota));
        order.setCost(newCost);
        log.debug("original price = {}, new price = {}", orderTotalAmount, newCost);
        return order;
    }

    // 金额计算具体逻辑，延迟到子类实现
    abstract protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota);

    // 计算订单总价
    protected long getTotalPrice(List<Product> products) {
        return products.stream()
                .mapToLong(product -> product.getPrice() * product.getCount())
                .sum();
    }

    // 根据门店维度计算每个门店下商品价格
    // key = shopId
    // value = 门店商品总价
    protected Map<Long, Long> getTotalPriceGroupByShop(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getShopId,
                        Collectors.summingLong(p -> p.getPrice() * p.getCount())));
    }

    // 每个订单最少支付1分钱
    protected long minCost() {
        return 1L;
    }

    protected long convertToDecimal(Double value) {
        return BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
