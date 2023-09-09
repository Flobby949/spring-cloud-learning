package top.flobby.coupon.customer.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description :
 * @create : 2023-09-09 10:32
 **/

@Getter
@AllArgsConstructor
public enum CouponStatus {
    AVAILABLE("可用", "0"),
    USED("已使用", "1"),
    INVALID("失效", "2");

    private final String status;
    private final String code;

    public static CouponStatus convert(String code) {
        return Stream.of(values())
                .filter(bean -> bean.code.equalsIgnoreCase(code))
                .findFirst()
                .orElse(INVALID);
    }
}
