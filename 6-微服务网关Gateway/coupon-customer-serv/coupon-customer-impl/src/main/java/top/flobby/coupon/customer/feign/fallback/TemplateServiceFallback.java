package top.flobby.coupon.customer.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flobby.coupon.customer.feign.TemplateService;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author : Flobby
 * @program : 2-Nacos+OpenFeign微服务调用
 * @description : 模板服务降级类
 * @create : 2023-09-15 13:55
 **/

@Slf4j
@Component
public class TemplateServiceFallback implements TemplateService {
    @Override
    public CouponTemplateInfo getTemplate(Long id) {
        log.info("******************");
        log.info("降级逻辑：根据id，获取模板");
        return null;
    }

    @Override
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(Collection<Long> ids) {
        log.info("******************");
        log.info("降级逻辑：批量获取模板");
        return Collections.emptyMap();
    }
}
