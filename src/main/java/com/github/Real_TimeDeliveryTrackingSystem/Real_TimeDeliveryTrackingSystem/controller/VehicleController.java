package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.VehicleControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.DriverService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Driver;

@RestController
@RequestMapping("/vehicle")
public class VehicleController implements VehicleControllerContract {

    private final DriverService service;

    @Autowired
    public VehicleController(DriverService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<VehicleVO> create(VehicleVO vehicleVO)  {
        VehicleVO createdVehicle = service.createVehicle(vehicleVO);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<VehicleVO> update(VehicleVO vehicleVO)  {
        VehicleVO updatedVehicle = service.updateVehicle(vehicleVO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @Override
    public ResponseEntity<VehicleVO> findById(String id) {
        VehicleVO vehicleVO = service.findVehicleById(id);
        return ResponseEntity.ok(vehicleVO);
    }

    @Override
    public ResponseEntity<VehicleVO> findByLicensePlate(String licensePlate) {
        VehicleVO byLicensePlate = service.findVehicleByLicensePlate(licensePlate);
        return ResponseEntity.ok(byLicensePlate);
    }

    @Override
    public ResponseEntity<Page<VehicleVO>> findAll(Pageable pageable){
        Page<VehicleVO> vehicles = service.findAllVehicles(pageable);
        return ResponseEntity.ok(vehicles);
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        service.deleteVehicle(id);
        return ResponseEntity.noContent().build();

    }
}
