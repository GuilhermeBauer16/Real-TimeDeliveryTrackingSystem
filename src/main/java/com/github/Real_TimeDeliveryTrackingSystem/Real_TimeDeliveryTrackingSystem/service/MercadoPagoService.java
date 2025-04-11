package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentRequest;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.common.AddressRequest;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.common.PhoneRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferencePaymentTypeRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.mercadopago.client.preference.PreferencePayerRequest.builder;

@Service
public class MercadoPagoService {

    @Value("${mercado-pago.access-token}")
    private String accessToken;

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public MercadoPagoService(ShoppingCartService productService) {
        this.shoppingCartService = productService;
    }

    public String createPreference(PaymentRequest paymentRequest) throws MPApiException {

        List<TemporaryProductEntity> temporaryProducts = shoppingCartService.findShoppingCart().getTemporaryProducts();

        MercadoPagoConfig.setAccessToken(accessToken);
        List<PreferenceItemRequest> items = new ArrayList<>();

        for(TemporaryProductEntity product : temporaryProducts) {

            System.out.println(product);

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(product.getId())
                    .title(product.getName())
                    .currencyId("BRL")
                    .pictureUrl("https://www.mercadopago.com/org-img/MP3/home/logomp3.gif")
                    .description(product.getDescription())
                    .categoryId("art")
                    .quantity(product.getQuantity())
                    .unitPrice(BigDecimal.valueOf(product.getPrice() / product.getQuantity()))
                    .build();

            System.out.println(itemRequest.toString());

            items.add(itemRequest);

        }






//        PreferencePayerRequest payer = builder()
//                .name("João")
//                .surname("Silva")
//                .email("test_user_1255791243@testuser.com")
//                .identification(IdentificationRequest.builder()
//                        .type("CPF")
//                        .number("12345678909")
//                        .build())
//                .phone(PhoneRequest.builder()
//                        .areaCode("11")
//                        .number("4444-4444")
//                        .build())
//                .address(AddressRequest.builder()
//                        .streetName("Street")
//                        .streetNumber("123")
//                        .zipCode("06233200")
//                        .build())
//                .build();
//
//        test_user_1255791243@testuser.com

        List<PreferencePaymentMethodRequest> excludedPaymentMethods = new ArrayList<>();
        excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("pec").build());

        List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();


        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .excludedPaymentMethods(excludedPaymentMethods)
                .excludedPaymentTypes(excludedPaymentTypes)
                .installments(12)
                .build();

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success("http://localhost:3000/payment/success")
                        .failure("http://localhost:3000/payment/failure")
                        .pending("http://localhost:3000/payment/pending").build())
                .autoReturn("approved")
                .paymentMethods(paymentMethods)
                .notificationUrl("https://4723-138-185-186-93.ngrok-free.app/ipn")
                .statementDescriptor("MEUNEGOCIO")
                .externalReference("Reference_1234")
                .expires(true)
                .build();
        // Set Mercado Pago credentials
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
//        PreferencePaymentMethodsRequest.PreferencePaymentMethodsRequestBuilder builder = PreferencePaymentMethodsRequest.builder();
//
//        builder.installments(7).build();
//
//
//
//        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
//                .items(Collections.singletonList(itemRequest))
//                .paymentMethods(builder.build())
//                .backUrls(PreferenceBackUrlsRequest.builder()
//                        .success("http://localhost:3000/payment/success")
//                        .failure("http://localhost:3000/payment/failure")
//                        .pending("http://localhost:3000/payment/pending")
//                        .build())
//                .autoReturn("approved") // Automatically returns after payment
//                .build();
//
        PreferenceClient client = new PreferenceClient();

        try {
            return client.create(preferenceRequest).getInitPoint();  // Get Mercado Pago payment URL
        } catch (MPApiException ex) {
            System.err.printf("MercadoPago API Error: Status: %d, Content: %s%n",
                    ex.getApiResponse().getStatusCode(), ex.getApiResponse().getContent());
            throw new MercadoPagoException("Mercado Pago API Error: " + ex.getApiResponse().getContent());
        } catch (MPException ex) {
            ex.printStackTrace();
            throw new MercadoPagoException("Internal Server Error");
        }
    }
//
    public static class MercadoPagoException extends RuntimeException {
        public MercadoPagoException(String message) {
            super(message);
        }
    }
    }
