package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String id;
    private List<TemporaryProductVO> temporaryProductEntities;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
}
