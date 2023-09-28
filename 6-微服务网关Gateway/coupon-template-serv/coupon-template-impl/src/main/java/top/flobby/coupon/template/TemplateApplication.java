package top.flobby.coupon.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 模板服务启动类
 * @create : 2023-09-08 16:00
 **/

@SpringBootApplication(scanBasePackages = {"top.flobby"})
@EnableJpaAuditing
public class TemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
