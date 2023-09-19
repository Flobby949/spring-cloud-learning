package top.flobby.coupon.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.flobby.coupon.customer.feign.fallback.TemplateServiceFallbackFactory;
import top.flobby.coupon.template.api.beans.CouponTemplateInfo;

import java.util.Collection;
import java.util.Map;

/**
 * @author : Flobby
 * @program : 2-Nacos+OpenFeign微服务调用
 * @description : 模板服务
 * @create : 2023-09-14 10:27
 **/

// @FeignClient(value = "coupon-template-serv", path = "/template", fallback = TemplateServiceFallback.class)
@FeignClient(value = "coupon-template-serv", path = "/template", fallbackFactory = TemplateServiceFallbackFactory.class)
public interface TemplateService {

    @GetMapping("/getTemplate")
    CouponTemplateInfo getTemplate(@RequestParam("id") Long id);

    @GetMapping("/getBatch")
    Map<Long, CouponTemplateInfo> getTemplateInBatch(@RequestParam("ids") Collection<Long> ids);
}
