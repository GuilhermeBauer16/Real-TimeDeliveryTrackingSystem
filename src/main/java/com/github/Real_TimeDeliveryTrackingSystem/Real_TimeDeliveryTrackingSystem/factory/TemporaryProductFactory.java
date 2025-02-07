package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;

public class TemporaryProductFactory {

    public static TemporaryProductEntity create(String id, String name, String description, Double price, Integer quantity) {
        return new TemporaryProductEntity(id, name, description, price, quantity);
    }


}
