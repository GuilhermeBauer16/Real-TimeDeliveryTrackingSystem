package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;

import java.util.UUID;

public class AddressFactory {


    public AddressFactory() {
    }

    public static AddressEntity create(String street, String city, String state, String postalCode, String country) {
        return new AddressEntity(UUID.randomUUID().toString(), street, city, state, postalCode, country);
    }
}
