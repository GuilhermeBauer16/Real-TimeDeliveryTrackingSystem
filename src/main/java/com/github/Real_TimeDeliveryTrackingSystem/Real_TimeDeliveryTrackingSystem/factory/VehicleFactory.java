package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;

import java.util.UUID;

public class VehicleFactory {

    public static VehicleEntity create(String name, String licensePlate, Type type, Status status ){
        return new VehicleEntity(UUID.randomUUID().toString(), name, licensePlate.toUpperCase(), type, status);
    }
}
