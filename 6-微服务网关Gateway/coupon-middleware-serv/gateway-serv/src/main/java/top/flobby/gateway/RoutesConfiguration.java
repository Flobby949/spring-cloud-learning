package top.flobby.gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;

/**
 * @author : Flobby
 * @program : 6-微服务网关 Gateway
 * @description : 使用 Java 代码进行路由配置
 * @create : 2023-09-28 08:49
 **/

// @Configuration
public class RoutesConfiguration {

    @Bean
    public RouteLocator declare(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(route -> route
                        // 在xxx时间点之后，可以正常访问，否则404
                        /// before 和 between 相同用法
                        .after(ZonedDateTime.parse("2022-12-12T14:13:30.678+08:00"))
                        .and()
                        .path("/gateway/coupon-customer/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://coupon-customer-serv")
                )
                .route(route -> route
                        .path("/gateway/template/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://coupon-template-serv")
                )
                .route(route -> route
                        .path("/gateway/calculator/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://coupon-calculation-serv")
                )
                .build();
    }
}
