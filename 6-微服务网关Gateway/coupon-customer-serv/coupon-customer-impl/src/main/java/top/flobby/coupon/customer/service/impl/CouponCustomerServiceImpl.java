package top.flobby.coupon.customer.service.impl;

import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.api.beans.SimulationOrder;
import top.flobby.coupon.calculation.api.beans.SimulationResponse;
import top.flobby.coupon.customer.api.beans.RequestCoupon;
import top.flobby.coupon.customer.api.beans.SearchCoupon;
import top.flobby.coupon.customer.api.enums.CouponStatus;
import top.flobby.coupon.customer.converter.CouponConverter;
import top.flobby.coupon.customer.dao.CouponDao;
import top.flobby.coupon.customer.dao.entity.Coupon;
import top.flobby.coupon.customer.feign.CalculationService;
import top.flobby.coupon.customer.feign.TemplateService;
import top.flobby.coupon.customer.service.CouponCustomerService;
import top.flobby.coupon.template.api.beans.CouponInfo;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;

import java.util.*;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description :
 * @create : 2023-09-09 10:53
 **/

@Slf4j
@Service
public class CouponCustomerServiceImpl implements CouponCustomerService {
    @Resource
    private CouponDao couponDao;

    @Resource
    private CalculationService calculationService;

    @Resource
    private TemplateService templateService;

    @Override
    public Coupon requestCoupon(RequestCoupon request) {
        CouponTemplateInfo templateInfo = templateService.getTemplate(request.getCouponTemplateId());

        // 模板不存在则报错
        if (templateInfo == null) {
            log.error("模板id不存在={}", request.getCouponTemplateId());
            throw new IllegalArgumentException("模板id不存在");
        }

        // 模板不能过期
        long now = Calendar.getInstance().getTimeInMillis();
        Long expTime = templateInfo.getRule().getDeadline();
        if (expTime != null && now >= expTime || BooleanUtils.isFalse(templateInfo.getAvailable())) {
            log.error("优惠券模板不可用={}", request.getCouponTemplateId());
            throw new IllegalArgumentException("优惠券模板不可用");
        }

        // 用户领券数量超过上限
        long count = couponDao.countByUserIdAndTemplateId(request.getUserId(), request.getCouponTemplateId());
        if (count >= templateInfo.getRule().getLimitation()) {
            log.error("超出领券最大数量");
            throw new IllegalArgumentException("超出领券最大数量");
        }

        Coupon coupon = Coupon.builder()
                .templateId(request.getCouponTemplateId())
                .userId(request.getUserId())
                .shopId(templateInfo.getShopId())
                .status(CouponStatus.AVAILABLE)
                .build();
        couponDao.save(coupon);
        return coupon;
    }

    @Override
    @Transactional
    public ShoppingCart placeOrder(ShoppingCart order) {
        if (CollectionUtils.isEmpty(order.getProducts())) {
            log.error("invalid check out request, order={}", order);
            throw new IllegalArgumentException("购物车为空");
        }

        Coupon coupon = null;
        if (order.getCouponId() != null) {
            // 如果有优惠券，验证是否可用，并且是当前客户的
            Coupon example = Coupon.builder()
                    .userId(order.getUserId())
                    .id(order.getCouponId())
                    .status(CouponStatus.AVAILABLE)
                    .build();
            coupon = couponDao.findAll(Example.of(example))
                    .stream()
                    .findFirst()
                    // 如果找不到券，就抛出异常
                    .orElseThrow(() -> new RuntimeException("找不到优惠券"));

            CouponInfo couponInfo = CouponConverter.convertToCoupon(coupon);
            couponInfo.setTemplate(templateService.getTemplate(coupon.getTemplateId()));
            order.setCouponInfos(Lists.newArrayList(couponInfo));
        }

        // WebClient调用计算服务进行 order清算
        ShoppingCart checkoutInfo = calculationService.checkout(order);

        if (coupon != null) {
            // 如果优惠券没有被结算掉，而用户传递了优惠券，报错提示该订单满足不了优惠条件
            if (CollectionUtils.isEmpty(Objects.requireNonNull(checkoutInfo).getCouponInfos())) {
                log.error("cannot apply coupon to order, couponId={}", coupon.getId());
                throw new IllegalArgumentException("订单不满足优惠条件");
            }

            log.info("update coupon status to used, couponId={}", coupon.getId());
            coupon.setStatus(CouponStatus.USED);
            couponDao.save(coupon);
        }

        return checkoutInfo;
    }

    @Override
    public SimulationResponse simulateOrderPrice(SimulationOrder order) {
        List<CouponInfo> couponInfos = Lists.newArrayList();
        // 挨个循环，把优惠券信息加载出来
        // 高并发场景下不能这么一个个循环，更好的做法是批量查询
        // 而且券模板一旦创建不会改内容，所以在创建端做数据异构放到缓存里，使用端从缓存捞template信息
        for (Long couponId : order.getCouponIds()) {
            Coupon example = Coupon.builder()
                    .userId(order.getUserId())
                    .id(couponId)
                    .status(CouponStatus.AVAILABLE)
                    .build();
            Optional<Coupon> couponOptional = couponDao.findAll(Example.of(example))
                    .stream()
                    .findFirst();
            // 加载优惠券模板信息
            if (couponOptional.isPresent()) {
                Coupon coupon = couponOptional.get();
                CouponInfo couponInfo = CouponConverter.convertToCoupon(coupon);
                couponInfo.setTemplate(templateService.getTemplate(coupon.getTemplateId()));
                couponInfos.add(couponInfo);
            }
        }
        order.setCouponInfos(couponInfos);

        // 调用接口试算服务
        return calculationService.simulate(order);
    }

    @Override
    public void deleteCoupon(Long userId, Long couponId) {
        Coupon example = Coupon.builder()
                .userId(userId)
                .id(couponId)
                .status(CouponStatus.AVAILABLE)
                .build();
        Coupon coupon = couponDao.findAll(Example.of(example))
                .stream()
                .findFirst()
                // 如果找不到券，就抛出异常
                .orElseThrow(() -> new RuntimeException("Could not find available coupon"));

        coupon.setStatus(CouponStatus.INACTIVE);
        couponDao.save(coupon);
    }

    @Override
    public List<CouponInfo> findCoupon(SearchCoupon request) {
        // 在真正的生产环境，这个接口需要做分页查询，并且查询条件要封装成一个类
        Coupon example = Coupon.builder()
                .userId(request.getUserId())
                .status(CouponStatus.convert(request.getCouponStatus()))
                .shopId(request.getShopId())
                .build();

        // TODO 这里你可以尝试实现分页查询
        List<Coupon> coupons = couponDao.findAll(Example.of(example));
        if (coupons.isEmpty()) {
            return Lists.newArrayList();
        }

        List<Long> templateIds = coupons.stream()
                .map(Coupon::getTemplateId)
                .toList();

        // 通过 WebClient 调用远程服务
        Map<Long, CouponTemplateInfo> templateMap = templateService.getTemplateInBatch(templateIds);

        coupons.forEach(e -> e.setTemplateInfo(Objects.requireNonNull(templateMap).get(e.getTemplateId())));

        return coupons.stream()
                .map(CouponConverter::convertToCoupon)
                .toList();
    }
}
