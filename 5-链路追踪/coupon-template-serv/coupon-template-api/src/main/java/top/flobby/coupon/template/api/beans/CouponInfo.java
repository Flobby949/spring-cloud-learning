package top.flobby.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 优惠券信息
 * @create : 2023-09-08 15:35
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfo {
    private Long id;
    private Long templateId;
    private Long userId;
    private Long shopId;
    private Integer status;
    private CouponTemplateInfo template;
}
