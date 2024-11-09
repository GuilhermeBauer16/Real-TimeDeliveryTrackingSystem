package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;

public interface VehicleServiceContract {

    VehicleVO create(VehicleVO vehicleVO);

    VehicleVO update(VehicleVO vehicleVO);

    VehicleVO findById(String id);

    void delete(String id);
}

