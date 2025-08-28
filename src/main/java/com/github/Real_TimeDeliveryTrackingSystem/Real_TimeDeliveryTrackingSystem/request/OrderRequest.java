package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request;

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
public class OrderRequest {

    private String customerEmail;
    private BigDecimal totalPrice;
    private List<TemporaryProductVO> temporaryProductVOS;
    private OrderStatus orderStatus;
}
