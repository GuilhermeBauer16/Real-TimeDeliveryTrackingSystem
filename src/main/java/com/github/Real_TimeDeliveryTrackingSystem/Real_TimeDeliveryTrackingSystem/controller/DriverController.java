package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.DriverControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driver")
public class DriverController implements DriverControllerContract {

    private final DriverService service;

    @Autowired
    public DriverController(DriverService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<Void> delete(PasswordDTO passwordDTO) {

        service.delete(passwordDTO);
        return ResponseEntity.noContent().build();
    }


}
