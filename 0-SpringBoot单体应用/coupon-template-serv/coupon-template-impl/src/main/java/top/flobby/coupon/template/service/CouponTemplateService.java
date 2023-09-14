package top.flobby.coupon.template.service;

import top.flobby.coupon.template.api.beans.CouponTemplateInfo;
import top.flobby.coupon.template.api.beans.PagedCouponTemplateInfo;
import top.flobby.coupon.template.api.beans.TemplateSearchParams;

import java.util.Collection;
import java.util.Map;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 定义了优惠券创建、查找优惠券和修改优惠券可用状态的方法。
 * @create : 2023-09-08 16:01
 **/

public interface CouponTemplateService {
    /**
     * 创建优惠券模板
     * @param request 模板信息
     * @return {@link CouponTemplateInfo}
     */
    CouponTemplateInfo createTemplate(CouponTemplateInfo request);


    /**
     * 通过模板id查找优惠券模板
     *
     * @param id 模板id
     * @return {@link CouponTemplateInfo}
     */
    CouponTemplateInfo loadTemplateInfo(Long id);

    /**
     * 克隆模板
     * @param templateId 模板id
     * @return {@link CouponTemplateInfo}
     */
    CouponTemplateInfo cloneTemplate(Long templateId);

    /**
     * 模板分页查询
     * @param request 查询参数
     * @return {@link PagedCouponTemplateInfo}
     */
    PagedCouponTemplateInfo search(TemplateSearchParams request);

    /**
     * 删除模板
     * @param id id
     */
    void deleteTemplate(Long id);

    /**
     * 批量读取模板
     *
     * @param ids ids
     * @return {@link Map}<{@link Long}, {@link CouponTemplateInfo}>
     *     Key为模板id，value是模板详情
     */
    Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids);
}
