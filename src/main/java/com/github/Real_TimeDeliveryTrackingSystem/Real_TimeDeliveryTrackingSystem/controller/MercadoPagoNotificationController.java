package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.MercadoPagoNotificationControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ipn")
public class MercadoPagoNotificationController implements MercadoPagoNotificationControllerContract {


    private final MercadoPagoService mercadoPagoService;

    @Autowired
    public MercadoPagoNotificationController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @Override
    public ResponseEntity<Void> handleWithNotificationPayment(String id, String topic) {

        mercadoPagoService.handlerWithApprovedPayment(id, topic);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
