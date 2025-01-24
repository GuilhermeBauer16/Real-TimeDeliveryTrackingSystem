package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;

import java.util.UUID;

public class ProductFactory {

    public static ProductEntity create(String name, String description, Double price,Integer quantity) {
        return new ProductEntity(UUID.randomUUID().toString(), name, description, price, quantity);
    }


}
