package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ipn")
public class MercadoPagoNotificationController {

    @Value("${mercado-pago.access-token}")
    private String accessToken;

    @PostMapping
    public ResponseEntity<String> handleNotification(@RequestParam("id") String id,
                                                     @RequestParam("topic") String topic) {
        if ("payment".equals(topic)) {
            MercadoPagoConfig.setAccessToken(accessToken);
            try {
                PaymentClient paymentClient = new PaymentClient();
                Payment payment = paymentClient.get(Long.parseLong(id));
                
                String status = payment.getStatus();
                System.out.println("Status do pagamento: " + status);

                if ("approved".equals(status)) {
                    // ✅ Atualize o status do pedido, envie e-mail, etc.
                }

            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro");
            }
        }
        return ResponseEntity.ok("Notificação recebida");
    }
}
