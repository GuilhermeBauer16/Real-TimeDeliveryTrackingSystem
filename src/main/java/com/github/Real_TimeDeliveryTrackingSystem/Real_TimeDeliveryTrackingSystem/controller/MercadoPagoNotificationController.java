package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.MercadoPagoService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final MercadoPagoService mercadoPagoService;

    @Autowired
    private TemporaryProductService temporaryProductService;

    @Autowired
    public MercadoPagoNotificationController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping
    public ResponseEntity<String> handleNotification(@RequestParam("id") String id,
                                                     @RequestParam("topic") String topic) {

        String s = mercadoPagoService.verifyIfPaymentWasApproved(id, topic);

        System.out.println("The notification payment status was: " + s);
//        if ("payment".equals(topic)) {
//            MercadoPagoConfig.setAccessToken(accessToken);
//            try {
//                PaymentClient paymentClient = new PaymentClient();
//                Payment payment = paymentClient.get(Long.parseLong(id));
//
//                String status = payment.getStatus();
//                System.out.println();
//                ;
//                System.out.println("Status do pagamento: " + status);
//
//                if ("approved".equals(status)) {
//                    System.out.println("The Payment was approved!!!!");
//                    PaymentAdditionalInfo additionalInfo = payment.getAdditionalInfo();
//                    List<PaymentItem> items = additionalInfo.getItems();
//
//                    for(PaymentItem item : items) {
//                        System.out.println(item.getId());
//                        TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(item.getId());
//                        System.out.println(temporaryProductById.getName());
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro");
//            }
//        }
//
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
}
