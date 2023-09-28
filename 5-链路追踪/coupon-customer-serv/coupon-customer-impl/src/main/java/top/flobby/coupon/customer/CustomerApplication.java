package top.flobby.coupon.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 启动类
 * @create : 2023-09-09 10:50
 **/

@SpringBootApplication(scanBasePackages = {"top.flobby"})
@EnableJpaAuditing
@EnableTransactionManagement
// 扫描 @Repository 注解
@EnableJpaRepositories(basePackages = {"top.flobby"})
// 扫描 JPA实体类
@EntityScan(basePackages = {"top.flobby"})
// OpenFeign 扫包
@EnableFeignClients(basePackages = {"top.flobby"})
@EnableHystrix
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
