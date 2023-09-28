package top.flobby.coupon.customer;

import feign.Logger;
import org.springframework.context.annotation.Bean;


/**
 * @author : Flobby
 * @description :
 * @create : 2023-09-14 09:16
 **/

@org.springframework.context.annotation.Configuration
public class Configuration {

    /**
     * 全级别log日志
     * @return Logger
     */
    @Bean
    Logger.Level feignLogger() {
        return Logger.Level.FULL;
    }
}
