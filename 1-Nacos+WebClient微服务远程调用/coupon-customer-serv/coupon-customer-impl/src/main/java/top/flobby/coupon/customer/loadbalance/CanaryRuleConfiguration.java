package top.flobby.coupon.customer.loadbalance;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author : Flobby
 * @program : 1-Nacos+WebClient微服务远程调用
 * @description : 金丝雀测试规则
 * @create : 2023-09-19 12:43
 **/

public class CanaryRuleConfiguration {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorLoadBalancer(Environment environment,
                                                                    LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        // 在Spring上下文中声明一个CanaryRule
        return new CanaryRule(loadBalancerClientFactory
                .getLazyProvider(name, ServiceInstanceListSupplier.class),
                name);
    }
}
