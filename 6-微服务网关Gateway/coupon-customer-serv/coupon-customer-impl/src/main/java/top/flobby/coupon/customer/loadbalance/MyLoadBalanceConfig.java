package top.flobby.coupon.customer.loadbalance;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author : Flobby
 * @program : 2-Nacos+OpenFeign微服务调用
 * @description :
 * @create : 2023-09-15 15:46
 **/


@LoadBalancerClients(defaultConfiguration = MyLoadBalanceConfig.class)
public class MyLoadBalanceConfig {

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorLoadBalancer(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new MyLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
