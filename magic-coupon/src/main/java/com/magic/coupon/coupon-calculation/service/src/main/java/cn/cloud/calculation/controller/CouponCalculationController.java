package cn.cloud.calculation.controller;

import cn.cloud.calculation.api.beans.ShoppingCart;
import cn.cloud.calculation.api.beans.SimulationOrder;
import cn.cloud.calculation.api.beans.SimulationResponse;
import cn.cloud.calculation.service.CouponCalculationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Cheng Yufei
 * @create 2023-03-23 18:47
 **/
@Slf4j
@RestController
@RequestMapping("calculator")
public class CouponCalculationController {

    @Autowired
    private CouponCalculationService calculationService;
    @Autowired
    private ObjectMapper objectMapper;

    private int a = 0;

    // 优惠券结算
    @PostMapping("/checkout")
    @ResponseBody
    public ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart settlement) throws JsonProcessingException {
        log.info("do calculation: {}",objectMapper.writeValueAsString(settlement));
        return calculationService.calculateOrderPrice(settlement);
    }

    /**
     * 优惠券列表挨个试算
     * 给客户提示每个可用券的优惠额度，帮助挑选
     * @param simulator
     * @return
     */
    @PostMapping("/simulate")
    @ResponseBody
    public SimulationResponse simulate(@RequestBody SimulationOrder simulator) throws JsonProcessingException {
        log.info("do simulation: {}", objectMapper.writeValueAsString(simulator));
        return calculationService.simulateOrder(simulator);
    }

}
