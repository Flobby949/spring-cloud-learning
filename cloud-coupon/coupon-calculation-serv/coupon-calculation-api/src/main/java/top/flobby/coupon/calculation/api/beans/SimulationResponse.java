package top.flobby.coupon.calculation.api.beans;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 价格试算结果
 * @create : 2023-09-08 17:01
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResponse {
    /**
     * 最省钱的优惠券id
     */
    private Long bestCouponId;
    /**
     * 每一个优惠券对应的订单价格
     */
    private Map<Long, Long> couponToOrderPrice = Maps.newHashMap();
}
