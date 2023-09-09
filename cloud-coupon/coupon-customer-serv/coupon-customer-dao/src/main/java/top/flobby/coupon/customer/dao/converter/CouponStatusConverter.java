package top.flobby.coupon.customer.dao.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import top.flobby.coupon.customer.api.enums.CouponStatus;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 优惠券状态转换类
 * @create : 2023-09-09 10:38
 **/

@Converter
public class CouponStatusConverter implements AttributeConverter<CouponStatus, String> {
    @Override
    public String convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(String code) {
        return CouponStatus.convert(code);
    }
}
