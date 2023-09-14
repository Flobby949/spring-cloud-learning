package top.flobby.coupon.calculation.api.beans;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.flobby.coupon.template.api.beans.CouponInfo;

import java.util.List;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 价格试算订单 - 最优订单
 * @create : 2023-09-08 17:00
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulationOrder {

    @NotEmpty
    private List<Product> productList;

    @NotEmpty
    private List<Long> couponIds;

    private List<CouponInfo> couponInfos;

    @NotNull
    private Long userId;
}
