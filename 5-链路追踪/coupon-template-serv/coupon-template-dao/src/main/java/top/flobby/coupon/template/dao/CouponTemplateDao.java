package top.flobby.coupon.template.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.flobby.coupon.template.dao.entity.CouponTemplate;

import java.util.List;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : CouponTemplateDao
 * @create : 2023-09-08 15:49
 **/

public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Long> {

    /**
     * 根据 ShopId 查询所有优惠券模板
     * @param shopId 店铺Id
     * @return {@link List}<{@link CouponTemplate}>
     */
    List<CouponTemplate> findAllByShopId(Long shopId);

    /**
     * IN 查询 + 分页
     * @param id 优惠券id
     * @param page 分页
     * @return {@link Page}<{@link CouponTemplate}>
     */
    Page<CouponTemplate> findAllByIdIn(List<Long> id, Pageable page);

    /**
     * 根据shopId + 可用状态查询店铺有多少优惠券模板
     *
     * @param shopId 店铺id
     * @param available 可用状态
     * @return {@link Integer}
     */
    Integer countByShopIdAndAvailable(Long shopId, Boolean available);

    /**
     * 将优惠券设置不可用
     * @param id 优惠券id
     * @return int 影响行数
     */
    @Modifying
    @Query("update CouponTemplate c set c.available = false where c.id = :id ")
    int makeCouponUnavailable(@Param("id") Long id);
}
