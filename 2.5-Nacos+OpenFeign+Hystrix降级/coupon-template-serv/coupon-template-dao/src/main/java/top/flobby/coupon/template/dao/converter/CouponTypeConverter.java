package top.flobby.coupon.template.dao.converter;

import top.flobby.coupon.template.api.enums.CouponType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 优惠券类型转换
 * @create : 2023-09-08 15:43
 **/

@Converter
public class CouponTypeConverter implements AttributeConverter<CouponType, String> {
    @Override
    public String convertToDatabaseColumn(CouponType couponType) {
        return couponType.getCode();
    }

    @Override
    public CouponType convertToEntityAttribute(String code) {
        return CouponType.convert(code);
    }
}
