package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public class CustomerFactory {

    public CustomerFactory() {
    }


    public static CustomerEntity create(String phoneNumber, List<AddressEntity> addresses, UserEntity user) {
        return new CustomerEntity(UUID.randomUUID().toString(), phoneNumber, addresses, user);
    }
}
