package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.MercadoPagoPaymentControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mercadoPago")
public class MercadoPagoPaymentController implements MercadoPagoPaymentControllerContract {

    private final MercadoPagoService paymentService;

    @Autowired
    public MercadoPagoPaymentController(MercadoPagoService paymentService) {
        this.paymentService = paymentService;
    }


    @Override
    public ResponseEntity<String> createPreference(PaymentRequest paymentRequest) throws MPException, MPApiException {
        String preferenceId = paymentService.createPreference();
        return ResponseEntity.ok(preferenceId);
    }


}