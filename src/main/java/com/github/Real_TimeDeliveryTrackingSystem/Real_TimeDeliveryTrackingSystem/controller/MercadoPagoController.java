package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mercadoPago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService paymentService; // Inject the service

    @PostMapping("/payment")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {

        try {
            String paymentStatus = paymentService.createPreference(paymentRequest);
            return ResponseEntity.ok(paymentStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MPApiException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/create")
    public Map<String, String> createPreference(@RequestBody PaymentRequest paymentRequest) throws MPApiException {
        String preferenceId = paymentService.createPreference(paymentRequest);
        Map<String, String> response = new HashMap<>();
        response.put("preferenceId", preferenceId);
        return response;
    }


}