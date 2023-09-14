package top.flobby.coupon.calculation.controller;

import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.flobby.coupon.calculation.api.beans.ShoppingCart;
import top.flobby.coupon.calculation.api.beans.SimulationOrder;
import top.flobby.coupon.calculation.api.beans.SimulationResponse;
import top.flobby.coupon.calculation.service.CouponCalculationService;

/**
 * @author : Flobby
 * @program : cloud-coupon
 * @description : controller
 * @create : 2023-09-08 17:14
 **/

@Slf4j
@RestController
@RequestMapping("calculator")
public class CouponCalculationController {
    @Resource
    private CouponCalculationService calculationService;

    /**
     * 优惠券结算
     *
     * @param settlement 结算信息
     * @return 结算结果
     */
    @PostMapping("/checkout")
    @ResponseBody
    public ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart settlement) {
        log.info("do calculation: {}", JSON.toJSONString(settlement));
        return calculationService.calculateOrderPrice(settlement);
    }

    /**
     * 优惠券列表挨个试算，给客户提示每个可用券的优惠额度，帮助挑选
     *
     * @param simulator 优惠券模拟器
     * @return 优惠券模拟结果
     */
    @PostMapping("/simulate")
    @ResponseBody
    public SimulationResponse simulate(@RequestBody SimulationOrder simulator) {
        log.info("do simulation: {}", JSON.toJSONString(simulator));
        return calculationService.simulateOrder(simulator);
    }
}
