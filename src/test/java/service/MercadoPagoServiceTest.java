package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.mercadoPago.MercadoPagoException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.mercadoPago.MercadoPagoService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.shoppingCart.ShoppingCartService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentAdditionalInfo;
import com.mercadopago.resources.payment.PaymentItem;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.eq;
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


    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private TemporaryProductService temporaryProductService;

    @Mock
    private ProductService productService;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private MercadoPagoService mercadoPagoService;

    @Mock
    private Payment payment;

    @Mock
    private PaymentItem paymentItem;


    @Mock
    private PaymentAdditionalInfo paymentAdditionalInfo;
    private ShoppingCartResponse shoppingCartResponse;

    @BeforeEach
    void setUp() {
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
        when(mockPreference.getInitPoint()).thenReturn("https://test-payment-link.com");

        try (MockedConstruction<PreferenceClient> mocked = mockConstruction(PreferenceClient.class,
                (mock, context) -> when(mock.create(any(PreferenceRequest.class))).thenReturn(mockPreference))) {

            when(shoppingCartService.findShoppingCart()).thenReturn(shoppingCartResponse);

            String result = mercadoPagoService.createPreference();

            assertNotNull(result);
            Assertions.assertEquals("https://test-payment-link.com", result);
        }
    }

    @Test
    void testCreatePreference_WhenShoppingCartIsEmpty_ShouldThrowShoppingCartNotFoundException() {

        when(shoppingCartService.findShoppingCart()).thenReturn(shoppingCartResponse);
        MercadoPagoException exception = assertThrows(MercadoPagoException.class, () -> mercadoPagoService.createPreference());

        assertNotNull(exception);
        Assertions.assertEquals(MercadoPagoException.ERROR.formatErrorMessage(MERCADO_PAGO_EXCEPTION_MESSAGE), exception.getMessage());
    }

    @Test
    void testHandlerWithApprovedPayment_WhenPaymentWasApproved_ShouldSendMailToUser() {


        Long validLongId = Long.parseLong(NUMERIC_ID);


        when(payment.getStatus()).thenReturn(PENDENT_PAYMENT_STATUS);


        try (MockedConstruction<PaymentClient> mocked = mockConstruction(PaymentClient.class,
                (mock, context) -> when(mock.get(validLongId)).thenReturn(payment))) {

            mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PAYMENT_TOPIC);

            verifyNoInteractions(emailSenderService);
            verifyNoInteractions(productService);
            verifyNoInteractions(shoppingCartService);
            verifyNoInteractions(temporaryProductService);


        }


    }

    @Test
    void testHandlerWithApprovedPayment_WhenStatusIsApproved_ShouldProcessProductAndSendEmail() throws Exception {

        String productId = "product-001";
        BigDecimal transactionAmount = BigDecimal.valueOf(50);

        PaymentItem item = mock(PaymentItem.class);
        when(item.getId()).thenReturn(productId);

        when(payment.getStatus()).thenReturn(APPROVED_PAYMENT_STATUS);
        when(payment.getAdditionalInfo()).thenReturn(paymentAdditionalInfo);
        when(payment.getTransactionAmount()).thenReturn(transactionAmount);
        when(paymentAdditionalInfo.getItems()).thenReturn(List.of(item));

        TemporaryProductVO temporaryProduct = new TemporaryProductVO();
        temporaryProduct.setId(productId);
        temporaryProduct.setQuantity(2);

        ProductVO product = new ProductVO();
        product.setId(productId);
        product.setQuantity(10);

        when(temporaryProductService.findTemporaryProductById(productId)).thenReturn(temporaryProduct);
        when(productService.findProductById(productId)).thenReturn(product);
        ReflectionTestUtils.setField(mercadoPagoService, "testMail", EMAIL);

        try (MockedConstruction<PaymentClient> mocked = mockConstruction(PaymentClient.class,
                (mock, context) -> when(mock.get(Long.parseLong(NUMERIC_ID))).thenReturn(payment))) {

            mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PAYMENT_TOPIC);

            verify(productService).updateProduct(argThat(updated ->
                    updated.getQuantity() == 8 && updated.getId().equals(productId)
            ));
            verify(shoppingCartService).deleteShoppingCart(EMAIL);
            verify(emailSenderService).sendMailToApprovedPayment(eq(EMAIL), anyList(), eq(transactionAmount));
        }
    }


    @Test
    void testHandlerWithApprovedPayment_WhenPaymentNotApproved_ShouldNotProcess() {


        try (MockedConstruction<PaymentClient> mocked = mockConstruction(PaymentClient.class,
                (mock, context) -> when(mock.get(Long.parseLong(NUMERIC_ID))).thenReturn(payment))) {

            mercadoPagoService.handlerWithApprovedPayment(NUMERIC_ID, PENDENT_PAYMENT_STATUS);

            verifyNoInteractions(productService, temporaryProductService, emailSenderService);
        }
    }

    @Test
    void testHandlerWithApprovedPayment_WhenTopicIsInvalid_ShouldDoNothing() {
        mercadoPagoService.handlerWithApprovedPayment("123", "invalid_topic");


        verifyNoInteractions(temporaryProductService, productService, shoppingCartService, emailSenderService);
    }


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

