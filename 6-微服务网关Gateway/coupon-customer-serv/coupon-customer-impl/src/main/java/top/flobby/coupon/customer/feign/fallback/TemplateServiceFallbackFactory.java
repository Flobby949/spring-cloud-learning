package top.flobby.coupon.customer.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.flobby.coupon.customer.feign.TemplateService;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;

import java.util.Collection;
import java.util.Map;

/**
 * @author : Flobby
 * @program : 2-Nacos+OpenFeign微服务调用
 * @description : 模板服务降级类
 * @create : 2023-09-15 13:55
 **/

@Slf4j
@Component
public class TemplateServiceFallbackFactory implements FallbackFactory<TemplateService> {

    @Override
    public TemplateService create(Throwable cause) {
        return new TemplateService() {
            @Override
            public CouponTemplateInfo getTemplate(Long id) {
                log.info("fallback factory method" + cause);
                return null;
            }

            @Override
            public Map<Long, CouponTemplateInfo> getTemplateInBatch(Collection<Long> ids) {
                log.info("fallback factory method" + cause);
                return null;
            }
        };
    }
}
