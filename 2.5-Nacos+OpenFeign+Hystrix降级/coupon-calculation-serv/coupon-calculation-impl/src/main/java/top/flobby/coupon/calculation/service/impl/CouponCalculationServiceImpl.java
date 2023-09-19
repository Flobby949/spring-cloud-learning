package top.flobby.coupon.calculation.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.api.beans.SimulationOrder;
import top.flobby.coupon.calculation.api.beans.SimulationResponse;
import top.flobby.coupon.calculation.service.CouponCalculationService;
import top.flobby.coupon.calculation.template.CouponTemplateFactory;
import top.flobby.coupon.calculation.template.RuleTemplate;
import top.flobby.coupon.template.api.beans.CouponInfo;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 实现类
 * @create : 2023-09-08 17:19
 **/

@Slf4j
@Service
public class CouponCalculationServiceImpl implements CouponCalculationService {
    @Resource
    private CouponTemplateFactory couponTemplateFactory;

    /**
     * 优惠券结算，通过工厂类决定使用哪个底层Rule，底层规则对上层透明
     *
     * @param cart 购物车
     * @return 结算后价格
     */
    @Override
    public ShoppingCart calculateOrderPrice(ShoppingCart cart) {
        log.info("计算订单价格：{}", JSON.toJSONString(cart));
        RuleTemplate ruleTemplate = couponTemplateFactory.getTemplate(cart);
        return ruleTemplate.calculate(cart);
    }

    /**
     * 对所有优惠券进行试算，看哪个最省钱
     *
     * @param order 购物车
     * @return 试算结果
     */
    @Override
    public SimulationResponse simulateOrder(SimulationOrder order) {
        SimulationResponse response = new SimulationResponse();
        long minOrderPrice = Long.MAX_VALUE;

        // 计算每一个优惠券的订单价格
        for (CouponInfo coupon : order.getCouponInfos()) {
            ShoppingCart cart = new ShoppingCart();
            cart.setProducts(order.getProductList());
            cart.setCouponInfos(Lists.newArrayList(coupon));
            cart = couponTemplateFactory.getTemplate(cart).calculate(cart);

            long couponId = coupon.getId();
            long orderPrice = cart.getCost();

            // 设置当前优惠券对应的订单价格
            response.getCouponToOrderPrice().put(couponId, orderPrice);
            // 比较订单价格，设置最优优惠券ID
            if (minOrderPrice > orderPrice) {
                response.setBestCouponId(coupon.getId());
                minOrderPrice = orderPrice;
            }
        }
        return response;
    }
}
