package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.mercadoPago.MercadoPagoException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer.KafkaProductProducer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.MercadoPagoServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.shoppingCart.ShoppingCartService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService implements MercadoPagoServiceContract {


    private final String accessToken;


    /**
     * <p>This variable is used to start an Ngrok server and is intended only for testing purposes.
     * In production, a better approach should be used.</p>
     */
    private final String ngrokUrl;

    private static final String CURRENCY = "BRL";
    private static final String PAYMENT_STATUS = "approved";
    private static final String PAYMENT_TOPIC = "payment";
    private static final String IMAGE_URL = "static/images/headphone";
    private static final String NOTIFICATION_URL = "/ipn";
    private static final String SUCCESS_URL = "/payment/success";
    private static final String PENDING_URL = "/payment/failure";
    private static final String FAILURE_URL = "/payment/pending";
    private static final String MERCADO_PAGO_EXCEPTION_MESSAGE = "Was not be possible to process the process with" +
            " Mercado Pago payment API";

    private final ShoppingCartService shoppingCartService;
    private final KafkaProductProducer kafkaProductProducer;

    @Autowired
    public MercadoPagoService(@Value("${mercado-pago.access-token}") String accessToken,
                              @Value("${mercado-pago.nrok-url}") String ngrokUrl,
                              ShoppingCartService productService, KafkaProductProducer kafkaProductProducer) {
        this.accessToken = accessToken;
        this.ngrokUrl = ngrokUrl;
        this.shoppingCartService = productService;
        this.kafkaProductProducer = kafkaProductProducer;
    }

    @Override
    public String createPreference() throws MPException {

        List<TemporaryProductEntity> temporaryProducts = shoppingCartService.findShoppingCart().getTemporaryProducts();

        MercadoPagoConfig.setAccessToken(accessToken);
        List<PreferenceItemRequest> items = new ArrayList<>();

        for (TemporaryProductEntity product : temporaryProducts) {


            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .id(product.getId())
                    .title(product.getName())
                    .currencyId(CURRENCY)
                    .pictureUrl(IMAGE_URL)
                    .description(product.getDescription())
                    .quantity(product.getQuantity())
                    .unitPrice(BigDecimal.valueOf(product.getPrice() / product.getQuantity()))
                    .build();


            items.add(itemRequest);

        }


        List<PreferencePaymentMethodRequest> excludedPaymentMethods = new ArrayList<>();
        excludedPaymentMethods.add(PreferencePaymentMethodRequest.builder().id("pec").build());


        PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                .excludedPaymentMethods(excludedPaymentMethods)
                .installments(12)
                .build();


        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success(ngrokUrl + SUCCESS_URL)
                        .failure(ngrokUrl + FAILURE_URL)
                        .pending(ngrokUrl + PENDING_URL).build())
                .autoReturn(PAYMENT_STATUS)
                .paymentMethods(paymentMethods)
                .notificationUrl(ngrokUrl + NOTIFICATION_URL)
                .expires(true)
                .build();

        PreferenceClient client = new PreferenceClient();


        try {

            return client.create(preferenceRequest).getInitPoint();

        } catch (MPApiException ex) {

            throw new MercadoPagoException(MERCADO_PAGO_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public void handlerWithApprovedPayment(String id, String topic) {

        if (PAYMENT_TOPIC.equals(topic)) {

            MercadoPagoConfig.setAccessToken(accessToken);
            try {

                PaymentClient paymentClient = new PaymentClient();
                Payment payment = paymentClient.get(Long.parseLong(id));


                if (payment.getStatus().equals(PAYMENT_STATUS)) {

                    kafkaProductProducer.sendProductMessage(generatePaymentProcessedRequest(payment));
                }


            } catch (Exception e) {
                throw new MercadoPagoException(MERCADO_PAGO_EXCEPTION_MESSAGE);

            }
        }


    }

    private PaymentProcessedRequest generatePaymentProcessedRequest(Payment payment) {

        List<String> productIds = new ArrayList<>();

        for (PaymentItem paymentItem : payment.getAdditionalInfo().getItems()) {
            productIds.add(paymentItem.getId());
        }
        return new PaymentProcessedRequest(payment.getPaymentMethodId(), payment.getPayer().getEmail(), payment.getTransactionAmount(), productIds);

    }


}


