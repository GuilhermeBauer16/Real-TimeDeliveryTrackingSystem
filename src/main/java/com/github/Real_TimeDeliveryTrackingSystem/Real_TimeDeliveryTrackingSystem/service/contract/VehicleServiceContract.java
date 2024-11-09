package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VehicleServiceContract {

    VehicleVO create(VehicleVO vehicleVO);

    VehicleVO update(VehicleVO vehicleVO);

    VehicleVO findById(String id);

    VehicleVO findByLicensePlate(String licensePlate);

    Page<VehicleVO> findAll(final Pageable pageable);

    void delete(String id);
}

