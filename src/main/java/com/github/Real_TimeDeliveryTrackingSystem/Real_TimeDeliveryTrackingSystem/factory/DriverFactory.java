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


    public static DriverEntity create(String phoneNumber, String licenseNumber, List<AddressEntity> addresses, UserEntity user,
                                      List<VehicleEntity> vehicles) {
        return new DriverEntity(UUID.randomUUID().toString(), phoneNumber, licenseNumber, addresses, user, vehicles);
    }
}
