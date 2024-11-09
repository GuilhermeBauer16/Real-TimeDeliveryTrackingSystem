package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.VehicleControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicle")
public class VehicleController implements VehicleControllerContract {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Override
    public ResponseEntity<VehicleVO> create(VehicleVO vehicleVO)  {
        VehicleVO createdVehicle = vehicleService.create(vehicleVO);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VehicleVO> update(VehicleVO vehicleVO)  {
        VehicleVO updatedVehicle = vehicleService.update(vehicleVO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @Override
    public ResponseEntity<VehicleVO> findById(String id) {
        VehicleVO vehicleVO = vehicleService.findById(id);
        return ResponseEntity.ok(vehicleVO);
    }

    @Override
    public ResponseEntity<Page<VehicleVO>> findAll(Pageable pageable)  {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        return null;
    }
}
