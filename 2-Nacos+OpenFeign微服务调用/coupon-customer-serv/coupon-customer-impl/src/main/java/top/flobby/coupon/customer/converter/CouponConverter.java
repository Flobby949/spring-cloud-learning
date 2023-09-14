package top.flobby.coupon.customer.converter;

import top.flobby.coupon.customer.dao.entity.Coupon;
import top.flobby.coupon.template.api.beans.CouponInfo;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 转换类
 * @create : 2023-09-09 10:52
 **/

public class CouponConverter {
    public static CouponInfo convertToCoupon(Coupon coupon) {
        return CouponInfo.builder()
                .id(coupon.getId())
                .status(coupon.getStatus().getCode())
                .templateId(coupon.getTemplateId())
                .shopId(coupon.getShopId())
                .userId(coupon.getUserId())
                .template(coupon.getTemplateInfo())
                .build();
    }
}
