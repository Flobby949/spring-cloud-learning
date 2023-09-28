package top.flobby.coupon.template.api.beans.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 模板规则
 * @create : 2023-09-08 15:08
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {
    /**
     * 可以享受折扣
     */
    private Discount discount;
    /**
     * 每个人最多可以领券数量
     */
    private Integer limitation;
    /**
     * 过期时间
     */
    private Long deadline;
}
