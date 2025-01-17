package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;

import java.util.List;
import java.util.UUID;

public class DriverFactory {

    public DriverFactory() {
    }


    public static DriverEntity create(String phoneNumber, String driverLicense, List<AddressEntity> addresses,
                                      List<VehicleEntity> vehicles, UserEntity user) {
        return new DriverEntity(UUID.randomUUID().toString(), phoneNumber, driverLicense, addresses, vehicles, user);
    }
}
