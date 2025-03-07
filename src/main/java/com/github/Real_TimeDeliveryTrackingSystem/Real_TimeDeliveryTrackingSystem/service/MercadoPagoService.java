//package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;
//
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentRequest;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
//import com.mercadopago.MercadoPagoConfig;
//import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
//import com.mercadopago.client.preference.PreferenceClient;
//import com.mercadopago.client.preference.PreferenceItemRequest;
//import com.mercadopago.client.preference.PreferenceRequest;
//import com.mercadopago.exceptions.MPApiException;
//import com.mercadopago.exceptions.MPException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.Collections;
//
//@Service
//public class MercadoPagoService {
//
//    @Value("${mercado-pago.access-token}")
//    private String accessToken;
//
//    private final TemporaryProductService productService;
//
//    @Autowired
//    public MercadoPagoService(TemporaryProductService productService) {
//        this.productService = productService;
//    }
//
//    public String createPreference(PaymentRequest paymentRequest) throws MPApiException {
//        // Set Mercado Pago credentials
//        MercadoPagoConfig.setAccessToken(accessToken);
//
//        TemporaryProductVO product = productService.findTemporaryProductById(paymentRequest.getProductId());
//
//        // Create item for the preference
//        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
//                .id(product.getId())
//                .title(product.getName())
//                .description(product.getDescription())
//                .quantity(product.getQuantity())
//                .currencyId("BRL")
//                .unitPrice(BigDecimal.valueOf(product.getPrice()))
//                .build();
//
//        // Create the preference request with redirect URLs
//        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
//                .items(Collections.singletonList(itemRequest))
//                .backUrls(PreferenceBackUrlsRequest.builder()
//                        .success("http://localhost:3000/payment/success")  // Redirect to success page
//                        .failure("http://localhost:3000/payment/failure")  // Redirect to failure page
//                        .pending("http://localhost:3000/payment/pending")  // Redirect if pending
//                        .build())
//                .autoReturn("approved") // Automatically returns after payment
//                .build();
//
//        PreferenceClient client = new PreferenceClient();
//
//        try {
//            return client.create(preferenceRequest).getInitPoint();  // Get Mercado Pago payment URL
//        } catch (MPApiException ex) {
//            System.err.printf("MercadoPago API Error: Status: %d, Content: %s%n",
//                    ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
//            throw new MercadoPagoException("Mercado Pago API Error: " + ex.getApiResponse().getContent());
//        } catch (MPException ex) {
//            ex.printStackTrace();
//            throw new MercadoPagoException("Internal Server Error");
//        }
//    }
//
//    public static class MercadoPagoException extends RuntimeException {
//        public MercadoPagoException(String message) {
//            super(message);
//        }
//    }
//}