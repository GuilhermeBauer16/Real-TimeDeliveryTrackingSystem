package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ShoppingCartEntity;

import java.util.List;
import java.util.UUID;

public class ShoppingCartFactory {

    public static ShoppingCartEntity create(CustomerEntity customerEntity, List<ProductEntity> products, Double totalPrice, List<ProductEntity> tempProducts) {
        return new ShoppingCartEntity(UUID.randomUUID().toString(),customerEntity, products, totalPrice, tempProducts);
    }


}
