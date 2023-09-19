package top.flobby.coupon.customer.api.beans;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description :
 * @create : 2023-09-09 10:31
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCoupon {
    @NotNull
    private Long userId;
    @NotNull
    private Long couponTemplateId;
}
