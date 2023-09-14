package top.flobby.coupon.template.api.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 分页信息
 * @create : 2023-09-08 15:37
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedCouponTemplateInfo {
    public List<CouponTemplateInfo> templates;
    public int page;
    public long total;
}
