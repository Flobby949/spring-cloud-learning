package top.flobby.coupon.calculation.api.beans;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.flobby.coupon.template.api.beans.CouponInfo;

import java.util.List;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 购物车
 * @create : 2023-09-08 16:58
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @NotEmpty
    private List<Product> products;

    private Long couponId;

    private long cost;

    /**
     * 目前只支持单张优惠券，为了以后的扩展考虑，可以添加多个优惠券计算逻辑
     */
    private List<CouponInfo> couponInfos;

    @NotNull
    private Long userId;
}
