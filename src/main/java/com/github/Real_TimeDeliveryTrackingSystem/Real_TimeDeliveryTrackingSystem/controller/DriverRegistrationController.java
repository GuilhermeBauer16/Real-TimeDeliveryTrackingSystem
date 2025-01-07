package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.DriverRegistrationControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.driver.DriverRegistrationService;
import com.google.i18n.phonenumbers.NumberParseException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signInDriver")
public class DriverRegistrationController implements DriverRegistrationControllerContract {

    private final DriverRegistrationService service;

    @Autowired
    public DriverRegistrationController(DriverRegistrationService service) {
        this.service = service;

    }


    @Override
    public ResponseEntity<DriverRegistrationResponse> registerCustomer(DriverVO driverVO) throws NumberParseException, MessagingException {

        DriverRegistrationResponse driverRegistrationResponse = service.create(driverVO);
        return new ResponseEntity<>(driverRegistrationResponse, HttpStatus.CREATED);
    }
}
