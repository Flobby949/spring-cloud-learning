package top.flobby.coupon.calculation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : 启动类
 * @create : 2023-09-08 17:13
 **/

@SpringBootApplication(scanBasePackages = {"top.flobby"})
public class CalculationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CalculationApplication.class, args);
    }
}
