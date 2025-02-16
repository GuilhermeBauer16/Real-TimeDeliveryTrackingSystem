package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MercadoPagoController {

    @Autowired
    private MercadoPagoService paymentService; // Inject the service

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {

        try {
            String paymentStatus = paymentService.processPayment(paymentRequest);
            return ResponseEntity.ok(paymentStatus);
        } catch (RuntimeException e) { // Catch the custom exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Return the specific error message
        }
    }
}