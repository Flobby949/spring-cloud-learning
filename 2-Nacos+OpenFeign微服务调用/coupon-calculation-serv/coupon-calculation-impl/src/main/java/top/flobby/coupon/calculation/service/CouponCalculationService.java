package top.flobby.coupon.calculation.service;

import org.springframework.web.bind.annotation.RequestBody;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.api.beans.SimulationOrder;
import top.flobby.coupon.calculation.api.beans.SimulationResponse;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 工厂方法根据订单中的优惠券信息，返回对应的Template用于计算优惠价格
 * @create : 2023-09-08 17:19
 **/

public interface CouponCalculationService {
    /**
     * 订单价格计算
     *
     * @param cart 购物车
     * @return {@link ShoppingCart}
     */
    ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart cart);

    /**
     * 订单价格试算
     *
     * @param cart 购物车
     * @return {@link SimulationOrder}
     */
    SimulationResponse simulateOrder(@RequestBody SimulationOrder cart);
}
