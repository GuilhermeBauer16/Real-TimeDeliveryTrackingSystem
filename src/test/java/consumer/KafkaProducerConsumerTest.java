package consumer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer.KafkaProductConsumer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.shoppingCart.ShoppingCartService;
import constants.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class KafkaProducerConsumerTest {

    private static final String EMAIL = "user@example.com";
    private static final BigDecimal TRANSITION_AMOUNT = BigDecimal.valueOf(TestConstants.SHOPPING_CART_TOTAL_PRICE);

    @Mock
    private ProductService productService;
    @Mock
    private TemporaryProductService temporaryProductService;
    @Mock
    private ShoppingCartService shoppingCartService;
    @Mock
    private EmailSenderService emailSenderService;

    PaymentProcessedRequest paymentProcessedRequest;
    TemporaryProductVO temporaryProductVO;
    ProductEntity productEntity;
    ProductVO productVO;

    @InjectMocks
    private KafkaProductConsumer kafkaProductConsumer;


    @BeforeEach
    void setUp() {


        ReflectionTestUtils.setField(kafkaProductConsumer, "testMail", EMAIL);

        temporaryProductVO = new TemporaryProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        productEntity = new ProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY + 2);

        productVO = new ProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, (TestConstants.PRODUCT_QUANTITY + 2));

        paymentProcessedRequest = new PaymentProcessedRequest(TestConstants.ID, EMAIL, TRANSITION_AMOUNT, List.of(TestConstants.ID));
    }

    @Test
    void testListenPaymentApprovedMessage_WhenMessageIsReceived_ShouldSendMailToApprovedPayment() {


        when(productService.findProductById(TestConstants.ID)).thenReturn(productVO);
        when(temporaryProductService.findTemporaryProductById(TestConstants.ID)).thenReturn(temporaryProductVO);

        kafkaProductConsumer.listenProductUpdate(paymentProcessedRequest);


        ArgumentCaptor<ProductVO> captor = ArgumentCaptor.forClass(ProductVO.class);
        verify(productService, times(1)).updateProduct(captor.capture());

        ProductVO updatedProduct = captor.getValue();

        Assertions.assertEquals(TestConstants.ID, updatedProduct.getId());
        Assertions.assertEquals(updatedProduct.getQuantity(), productVO.getQuantity());

        verify(shoppingCartService).deleteShoppingCart(EMAIL);
        verify(emailSenderService).sendMailWithApprovedPaymentToKafkaProducer(
                EMAIL, List.of(temporaryProductVO), TRANSITION_AMOUNT
        );
    }


}
