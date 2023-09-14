package top.flobby.coupon.customer.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.flobby.coupon.customer.dao.entity.Coupon;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description :
 * @create : 2023-09-09 10:38
 **/

public interface CouponDao extends JpaRepository<Coupon, Long> {

    long countByUserIdAndTemplateId(Long userId, Long templateId);
}
