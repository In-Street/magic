package cn.cloud.calculation.service;

import cn.cloud.calculation.api.beans.ShoppingCart;
import cn.cloud.calculation.api.beans.SimulationOrder;
import cn.cloud.calculation.api.beans.SimulationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Cheng Yufei
 * @create 2023-03-23 18:27
 **/
public interface CouponCalculationService {
    ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart cart) throws JsonProcessingException;

    SimulationResponse simulateOrder(@RequestBody SimulationOrder cart);
}
