package top.flobby.coupon.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.api.beans.SimulationOrder;
import top.flobby.coupon.calculation.api.beans.SimulationResponse;

/**
 * @author : Flobby
 * @program : 2-Nacos+OpenFeign微服务调用
 * @description : 模板服务
 * @create : 2023-09-14 10:27
 **/

@FeignClient(value = "coupon-calculation-serv", path = "/calculator")
public interface CalculationService {

    @PostMapping("/checkout")
    ShoppingCart checkout(ShoppingCart settlement);

    @PostMapping("/simulate")
    SimulationResponse simulate(SimulationOrder simulation);
}
