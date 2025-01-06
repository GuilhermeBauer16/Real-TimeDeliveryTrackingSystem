package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.CustomerRegistrationControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerRegistrationService;
import com.google.i18n.phonenumbers.NumberParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signInCustomer")
public class CustomerRegistrationController implements CustomerRegistrationControllerContract {

    private final CustomerRegistrationService service;

    @Autowired
    public CustomerRegistrationController(CustomerRegistrationService service) {
        this.service = service;

    }

    @Override
    public ResponseEntity<CustomerRegistrationResponse> registerCustomer(CustomerVO customerVO) throws NumberParseException {

        CustomerRegistrationResponse createdCustomer = service.create(customerVO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }
}
