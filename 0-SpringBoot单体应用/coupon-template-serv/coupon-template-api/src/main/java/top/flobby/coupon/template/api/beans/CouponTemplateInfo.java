package top.flobby.coupon.template.api.beans;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.flobby.coupon.template.api.beans.rules.TemplateRule;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 封装优惠券模板的基本信息
 * @create : 2023-09-08 15:08
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplateInfo {
    private Long id;

    @NotNull
    private String name;

    /**
     * 优惠券描述
     */
    @NotNull
    private String desc;

    /**
     * 优惠券类型
     */
    @NotNull
    private String type;

    /**
     * 适用门店 - 若无则为全店通用券
     */
    private Long shopId;

    /**
     * 优惠券规则
     */
    @NotNull
    private TemplateRule rule;

    /**
     * 当前模板是否为可用状态
     */
    private Boolean available;
}
