package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.mercadoPago.MercadoPagoException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer.KafkaProductProducer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.shoppingCart.ShoppingCartService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentAdditionalInfo;
import com.mercadopago.resources.payment.PaymentItem;
import com.mercadopago.resources.payment.PaymentPayer;
import com.mercadopago.resources.preference.Preference;
import constants.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MercadoPagoServiceTest {


    private static final String MERCADO_PAGO_EXCEPTION_MESSAGE = "Was not be possible to process the process with" +
            " Mercado Pago payment API";


    private static final String APPROVED_PAYMENT_STATUS = "approved";
    private static final String PENDENT_PAYMENT_STATUS = "pendent";
    private static final String PAYMENT_TOPIC = "payment";
    private static final String EMAIL = "test@example.com";
    private static final String NUMERIC_ID = "123";
    private static final String PAYMENT_URL = "https://test-payment-link.com";
    private static final String INVALID_PAYMENT_TOPIC = "invalid-payment-topic";
    private static final BigDecimal TRANSACTION_AMOUNT = BigDecimal.valueOf(TestConstants.PRODUCT_PRICE);


    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private Payment payment;

    @Mock
    private PaymentPayer payer;

    @Mock
    private PaymentItem paymentItem;
    @Mock
    private KafkaProductProducer kafkaProductProducer;

    @Mock
    private PaymentAdditionalInfo paymentAdditionalInfo;

    private ShoppingCartResponse shoppingCartResponse;

    @InjectMocks
    private MercadoPagoService mercadoPagoService;

    @BeforeEach
    void setUp() {

//        paymentProcessedRequest = new PaymentProcessedRequest(TestConstants.ID, EMAIL, TRANSACTION_AMOUNT, List.of(TestConstants.ID, TestConstants.ID));
        TemporaryProductEntity temporaryProductEntity = new TemporaryProductEntity(
                TestConstants.ID,
                TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION,
                TestConstants.PRODUCT_PRICE,
                TestConstants.PRODUCT_QUANTITY
        );


        shoppingCartResponse = new ShoppingCartResponse(
                TestConstants.ID,
                TestConstants.SHOPPING_CART_TOTAL_PRICE,
                List.of(temporaryProductEntity)
        );


    }

    @Test
    void testCreatePreference_WhenSuccessful_ShouldReturnPaymentLink() throws MPException {

        Preference mockPreference = mock(Preference.class);
        when(mockPreference.getInitPoint()).thenReturn(PAYMENT_URL);

        try (MockedConstruction<PreferenceClient> mocked = mockConstruction(PreferenceClient.class,
                (mock, context) -> when(mock.create(any(PreferenceRequest.class))).thenReturn(mockPreference))) {

            when(shoppingCartService.findShoppingCart()).thenReturn(shoppingCartResponse);

            String result = mercadoPagoService.createPreference();

            assertNotNull(result);
            Assertions.assertEquals(PAYMENT_URL, result);
        }
    }

    @Test
    void testCreatePreference_WhenHaveProblemToCreateAPreference_ShouldThrowMercadoPagoException() {

        when(shoppingCartService.findShoppingCart()).thenReturn(shoppingCartResponse);
        MercadoPagoException exception = assertThrows(MercadoPagoException.class, () -> mercadoPagoService.createPreference());

        assertNotNull(exception);
        Assertions.assertEquals(MercadoPagoException.ERROR.formatErrorMessage(
                MERCADO_PAGO_EXCEPTION_MESSAGE), exception.getMessage());
    }

    @Test
    void testHandlerWithApprovedPayment_WhenPaymentWasApproved_ShouldSendMailToUser() {


        when(payment.getStatus()).thenReturn(PENDENT_PAYMENT_STATUS);


        try (MockedConstruction<PaymentClient> mocked = mockConstruction(PaymentClient.class,
                (mock, context) -> when(mock.get(Long.parseLong(NUMERIC_ID))).thenReturn(payment))) {

            mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PAYMENT_TOPIC);

            verifyNoInteractions(shoppingCartService);


        }


    }

    @Test
    void testHandlerWithApprovedPayment_WhenApproved_ShouldBuildAndSendCorrectPaymentProcessedRequest() {


        when(paymentItem.getId()).thenReturn(TestConstants.ID);
        when(paymentAdditionalInfo.getItems()).thenReturn(List.of(paymentItem));
        when(payment.getStatus()).thenReturn(APPROVED_PAYMENT_STATUS);
        when(payment.getTransactionAmount()).thenReturn(TRANSACTION_AMOUNT);
        when(payment.getPayer()).thenReturn(payer);
        when(payment.getPayer().getEmail()).thenReturn(EMAIL);
        when(payment.getAdditionalInfo()).thenReturn(paymentAdditionalInfo);

        try (MockedConstruction<PaymentClient> mocked = mockConstruction(PaymentClient.class,
                (mock, context) -> when(mock.get(Long.parseLong(NUMERIC_ID))).thenReturn(payment))) {

            mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PAYMENT_TOPIC);


            verify(kafkaProductProducer).sendProductMessage(argThat(request ->
                    request.getCustomerEmail().equals(EMAIL) &&
                            request.getTransitionalAmount().compareTo(TRANSACTION_AMOUNT) == 0 &&
                            request.getProductIds().size() == 1 &&
                            request.getProductIds().getFirst().equals(TestConstants.ID)
            ));
        }
    }

    @Test
    void testHandlerWithApprovedPayment_WhenTopicIsInvalid_ShouldDoNothing() {
        mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, INVALID_PAYMENT_TOPIC);
        verifyNoInteractions(kafkaProductProducer);
    }

//    @Test
//    void testHandlerWithApprovedPayment_WhenStatusIsApproved_ShouldProcessProductAndSendEmail() {
//
//        temporaryProductVO.setQuantity(4);
//        when(paymentItem.getId()).thenReturn(TestConstants.ID);
//        when(payment.getStatus()).thenReturn(APPROVED_PAYMENT_STATUS);
//        when(payment.getAdditionalInfo()).thenReturn(paymentAdditionalInfo);
//        when(payment.getTransactionAmount()).thenReturn(TRANSACTION_AMOUNT);
//        when(paymentAdditionalInfo.getItems()).thenReturn(List.of(paymentItem));
//
//        when(temporaryProductService.findTemporaryProductById(TestConstants.ID)).thenReturn(temporaryProductVO);
//        when(productService.findProductById(TestConstants.ID)).thenReturn(productVO);
//
//        ReflectionTestUtils.setField(mercadoPagoService, "testMail", EMAIL);
//
//        try (MockedConstruction<PaymentClient> mocked = mockConstruction(PaymentClient.class,
//                (mock, context) -> when(mock.get(Long.parseLong(NUMERIC_ID))).thenReturn(payment))) {
//
//            mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PAYMENT_TOPIC);
//
//            verify(productService).updateProduct(argThat(updated ->
//                    updated.getQuantity() == 8 && updated.getId().equals(TestConstants.ID)
//            ));
//            verify(shoppingCartService).deleteShoppingCart(EMAIL);
//        }
//    }


//    @Test
//    void testHandlerWithApprovedPayment_WhenPaymentNotApproved_ShouldNotProcess() {
//
//        mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PENDENT_PAYMENT_STATUS);
//
//        verifyNoInteractions(productService, temporaryProductService, emailSenderService);
//
//    }
//
//    @Test
//    void testHandlerWithApprovedPayment_WhenTopicIsInvalid_ShouldDoNothing() {
//        mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, INVALID_PAYMENT_TOPIC);
//
//        verifyNoInteractions(temporaryProductService, productService, shoppingCartService, emailSenderService);
//    }


    @Test
    void testHandlerWithApprovedPayment_WhenWasProblemWithPaymentApproved_ShouldThrowMercadoPagoException() {

        MercadoPagoException exception = assertThrows(MercadoPagoException.class,
                () -> mercadoPagoService.handlerWithApprovedPayment(TestConstants.ID, PAYMENT_TOPIC));

        assertNotNull(exception);
        assertEquals(MercadoPagoException.ERROR.formatErrorMessage(MERCADO_PAGO_EXCEPTION_MESSAGE),
                exception.getMessage()
        );
    }


}

