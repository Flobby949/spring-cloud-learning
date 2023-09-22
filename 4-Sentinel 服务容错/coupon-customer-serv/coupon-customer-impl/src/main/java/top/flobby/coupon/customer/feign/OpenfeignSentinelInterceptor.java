package top.flobby.coupon.customer.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Flobby
 * @program : 4-Sentinel 服务容错
 * @description : 拦截器
 * @create : 2023-09-22 14:02
 **/

@Configuration
public class OpenfeignSentinelInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 传递给下游服务的标记
        requestTemplate.header("SentinelSource", "coupon-customer-serv");
    }
}
