package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.MercadoPagoException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferencePaymentTypeRequest;
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
public class MercadoPagoService {

    @Value("${mercado-pago.access-token}")
    private String accessToken;

    private static final String PAYMENT_STATUS = "approved";
    private static final String PAYMENT_TOPIC = "payment";
    private static final String NOTIFICATION_URL = "https://868a-138-185-184-181.ngrok-free.app/ipn";
    private static final String SUCCESS_URL = "http://localhost:3000/payment/success";
    private static final String PENDING_URL = "http://localhost:3000/payment/failure";
    private static final String FAILURE_URL = "http://localhost:3000/payment/pending";
    private static final String MERCADO_PAGO_EXCEPTION_MESSAGE = "Was not be possible to process the Mercado Pago payment API";

    private final ShoppingCartService shoppingCartService;
    private final TemporaryProductService temporaryProductService;
    private final ProductService productService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public MercadoPagoService(ShoppingCartService productService, TemporaryProductService temporaryProductService, ProductService productService1, EmailSenderService emailSenderService) {
        this.shoppingCartService = productService;
        this.temporaryProductService = temporaryProductService;
        this.productService = productService1;
        this.emailSenderService = emailSenderService;
    }

    public String createPreference() throws MPApiException, MPException {

        List<TemporaryProductEntity> temporaryProducts = shoppingCartService.findShoppingCart().getTemporaryProducts();

        MercadoPagoConfig.setAccessToken(accessToken);
        List<PreferenceItemRequest> items = new ArrayList<>();

        for (TemporaryProductEntity product : temporaryProducts) {


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


            items.add(itemRequest);

        }


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
                        .success(SUCCESS_URL)
                        .failure(FAILURE_URL)
                        .pending(PENDING_URL).build())
                .autoReturn(PAYMENT_STATUS)
                .paymentMethods(paymentMethods)
                .notificationUrl(NOTIFICATION_URL)
                .statementDescriptor("MEUNEGOCIO")
                .externalReference("Reference_1234")
                .expires(true)
                .build();

        PreferenceClient client = new PreferenceClient();


        try {

            return client.create(preferenceRequest).getInitPoint();

        } catch (MPApiException ex) {

            throw new MercadoPagoException(MERCADO_PAGO_EXCEPTION_MESSAGE);
        }
    }

    public String verifyIfPaymentWasApproved(String id, String topic) {

        if (PAYMENT_TOPIC.equals(topic)) {

            MercadoPagoConfig.setAccessToken(accessToken);
            try {

                PaymentClient paymentClient = new PaymentClient();
                Payment payment = paymentClient.get(Long.parseLong(id));


                if (payment.getStatus().equals(PAYMENT_STATUS)) {

                    List<PaymentItem> items = payment.getAdditionalInfo().getItems();
                    Double totalPrice = 0D;


                    if (!items.isEmpty()) {
                        List<TemporaryProductVO> temporaryProductVOS = new ArrayList<>();
                        for (PaymentItem paymentItem : items) {


                            TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(paymentItem.getId());
                            ProductVO productById = productService.findProductById(paymentItem.getId());

                            productById.setQuantity(productById.getQuantity() - temporaryProductById.getQuantity());
                            productService.updateProduct(productById);
                            totalPrice += temporaryProductById.getPrice();

                            temporaryProductVOS.add(temporaryProductById);

                        }

                        shoppingCartService.deleteShoppingCart(payment.getPayer().getEmail());
                        emailSenderService.sendMailToApprovedPayment(payment.getPayer().getEmail(), temporaryProductVOS, totalPrice);

                    }


                    return payment.getStatus();


                }


            } catch (Exception e) {
                e.printStackTrace();
                return RuntimeException.class.getSimpleName();
            }
        }

        return topic;


    }

}


