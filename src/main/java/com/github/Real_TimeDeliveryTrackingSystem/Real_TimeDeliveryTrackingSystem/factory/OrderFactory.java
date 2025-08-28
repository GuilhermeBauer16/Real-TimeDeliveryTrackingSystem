package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.OrderEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class OrderFactory {

    public OrderFactory() {
    }

    public static OrderEntity create(CustomerEntity customer, List<TemporaryProductEntity> temporaryProductEntities,
                                     BigDecimal totalPrice, OrderStatus orderStatus) {

        return new OrderEntity(UUID.randomUUID().toString(), customer, temporaryProductEntities, totalPrice, orderStatus);
    }


}
