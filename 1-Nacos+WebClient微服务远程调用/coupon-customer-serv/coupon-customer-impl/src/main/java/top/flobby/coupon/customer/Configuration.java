package top.flobby.coupon.customer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author : Flobby
 * @program : 1-Nacos+WebClient微服务远程调用
 * @description :
 * @create : 2023-09-14 09:16
 **/

@org.springframework.context.annotation.Configuration
public class Configuration {

    /**
     * 注册一个 WebClient 实例，用来远程调用
     * @return WebClient
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder register() {
        return WebClient.builder();
    }
}
