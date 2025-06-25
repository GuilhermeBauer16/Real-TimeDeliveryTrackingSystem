package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.mercadoPago.MercadoPagoException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.MercadoPagoServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
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
import jakarta.mail.MessagingException;
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
     * <p>This variable is only used in test environment,
     * in production environment change the use for the method</p>
     * <h3>Production Example:</h3>
     * <pre> {@code
     *  PaymentClient paymentClient = new PaymentClient();
     *  Payment payment = paymentClient.get(Long.parseLong(id));
     *  payment.getPayer().getEmail();}</pre>
     * <h4>Put the <pre>{@code payment.getPayer().getEmail(); } </pre> in the place of the variable testMail</h4>
     */
    private final String testMail;

    /**
     * <p>This variable is used to start an Ngrok server and is intended only for testing purposes.
     * In production, a better approach should be used.</p>
     */
    private final String nrokUrl;

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
    private final TemporaryProductService temporaryProductService;
    private final ProductService productService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public MercadoPagoService(@Value("${mercado-pago.access-token}") String accessToken, @Value("${mercado-pago.test-mail}") String testMail,
                              @Value("${mercado-pago.nrok-url}") String nrokUrl,
                              ShoppingCartService productService, TemporaryProductService temporaryProductService, ProductService productService1, EmailSenderService emailSenderService) {
        this.accessToken = accessToken;
        this.testMail = testMail;
        this.nrokUrl = nrokUrl;
        this.shoppingCartService = productService;
        this.temporaryProductService = temporaryProductService;
        this.productService = productService1;
        this.emailSenderService = emailSenderService;
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
                        .success(nrokUrl + SUCCESS_URL)
                        .failure(nrokUrl + FAILURE_URL)
                        .pending(nrokUrl + PENDING_URL).build())
                .autoReturn(PAYMENT_STATUS)
                .paymentMethods(paymentMethods)
                .notificationUrl(nrokUrl + NOTIFICATION_URL)
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
                    handlerWithProductProcess(payment);
                }


            } catch (Exception e) {
                throw new MercadoPagoException(MERCADO_PAGO_EXCEPTION_MESSAGE);

            }
        }


    }

    private void handlerWithProductProcess(Payment payment) throws MessagingException {
        List<PaymentItem> items = payment.getAdditionalInfo().getItems();


        List<TemporaryProductVO> temporaryProductVOS = new ArrayList<>();
        for (PaymentItem paymentItem : items) {


            TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(paymentItem.getId());
            ProductVO productById = productService.findProductById(paymentItem.getId());
            productById.setQuantity(productById.getQuantity() - temporaryProductById.getQuantity());
            productService.updateProduct(productById);
            temporaryProductVOS.add(temporaryProductById);

        }

        shoppingCartService.deleteShoppingCart(testMail);
        emailSenderService.doSendMailToApprovedPayment(testMail, temporaryProductVOS, payment.getTransactionAmount());


    }

}


