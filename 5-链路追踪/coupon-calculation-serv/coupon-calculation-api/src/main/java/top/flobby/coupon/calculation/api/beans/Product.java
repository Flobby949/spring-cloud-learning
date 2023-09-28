package top.flobby.coupon.calculation.api.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 加购产品
 * @create : 2023-09-08 16:56
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 价格
     */
    private long price;

    /**
     * 商品在购物车的数量
     */
    private Integer count;

    /**
     * 店铺id
     */
    private Long shopId;
}
