package top.flobby.coupon.template.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;
import top.flobby.coupon.template.api.beans.PagedCouponTemplateInfo;
import top.flobby.coupon.template.api.beans.TemplateSearchParams;
import top.flobby.coupon.template.api.enums.CouponType;
import top.flobby.coupon.template.converter.CouponTemplateConverter;
import top.flobby.coupon.template.dao.CouponTemplateDao;
import top.flobby.coupon.template.dao.entity.CouponTemplate;
import top.flobby.coupon.template.service.CouponTemplateService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 实现类
 * @create : 2023-09-08 16:06
 **/

@Slf4j
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {
    @Resource
    private CouponTemplateDao templateDao;


    @Override
    public CouponTemplateInfo cloneTemplate(Long templateId) {
        log.info("cloning template id {}", templateId);
        CouponTemplate source = templateDao.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("非法Id"));

        CouponTemplate target = new CouponTemplate();
        BeanUtils.copyProperties(source, target);

        target.setAvailable(false);
        target.setId(null);

        templateDao.save(target);
        return CouponTemplateConverter.convertToTemplateInfo(target);
    }

    @Override
    public CouponTemplateInfo createTemplate(CouponTemplateInfo request) {
        // 单个门店最多可以创建100张优惠券模板
        if (request.getShopId() != null) {
            Integer count = templateDao.countByShopIdAndAvailable(request.getShopId(), true);
            if (count >= 100) {
                log.error("超过门店创建优惠券模板数量最大限制！");
                throw new UnsupportedOperationException("超过门店创建优惠券模板数量最大限制！");
            }
        }

        // 创建优惠券
        CouponTemplate template = templateDao.saveAndFlush(CouponTemplate.builder()
                .name(request.getName())
                .description(request.getDesc())
                .category(CouponType.convert(request.getType()))
                .available(true)
                .shopId(request.getShopId())
                .rule(request.getRule())
                .build());
        return CouponTemplateConverter.convertToTemplateInfo(template);
    }

    @Override
    public CouponTemplateInfo loadTemplateInfo(Long id) {
        Optional<CouponTemplate> template = templateDao.findById(id);
        return template.map(CouponTemplateConverter::convertToTemplateInfo).orElse(null);
    }

    @Override
    public PagedCouponTemplateInfo search(TemplateSearchParams request) {
        CouponTemplate example = CouponTemplate.builder()
                .shopId(request.getShopId())
                .category(CouponType.convert(request.getType()))
                .available(request.getAvailable())
                .name(request.getName())
                .build();

        Pageable page = PageRequest.of(request.getPage(), request.getPageSize());
        Page<CouponTemplate> result = templateDao.findAll(Example.of(example), page);
        List<CouponTemplateInfo> couponTemplateInfos = result.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                .toList();

        return PagedCouponTemplateInfo.builder()
                .templates(couponTemplateInfos)
                .page(request.getPage())
                .total(result.getTotalElements())
                .build();
    }

    /**
     * 分页查询模板
     *
     * @param request 请求参数
     * @return list
     */
    public List<CouponTemplateInfo> searchTemplate(CouponTemplateInfo request) {
        CouponTemplate example = CouponTemplate.builder()
                .shopId(request.getShopId())
                .category(CouponType.convert(request.getType()))
                .available(request.getAvailable())
                .name(request.getName())
                .build();
        // 分页
        // Pageable page = PageRequest.of(request.getPage(), request.getPageSize());
        // Page<CouponTemplate> result = templateDao.findAll(Example.of(example), page);
        List<CouponTemplate> result = templateDao.findAll(Example.of(example));
        return result.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                .toList();
    }

    @Override
    public void deleteTemplate(Long id) {
        int rows = templateDao.makeCouponUnavailable(id);
        if (rows == 0) {
            throw new IllegalArgumentException("模板不存在：" + id);
        }
    }

    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInfoMap(Collection<Long> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
        return templates.stream()
                .map(CouponTemplateConverter::convertToTemplateInfo)
                .collect(Collectors.toMap(CouponTemplateInfo::getId, Function.identity()));
    }
}
