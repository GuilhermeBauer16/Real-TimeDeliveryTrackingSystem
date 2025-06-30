package producer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer.KafkaEmailProducer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaEmailProducerTest {

    private static final String EMAIL = "user@example.com";
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(TestConstants.SHOPPING_CART_TOTAL_PRICE);
    private static final String KAFKA_EMAIL_TOPIC = "email-verification-topic";
    private static final String KAFKA_PAYMENT_APPROVED_TOPIC = "payment-approved-topic";

    private EmailVerificationMessageRequest emailVerificationMessageRequest;
    private PaymentApprovedMessageRequest paymentApprovedMessageRequest;

    @Mock
    private KafkaTemplate<String, EmailVerificationMessageRequest> emailKafkaTemplate;

    @Mock
    private KafkaTemplate<String, PaymentApprovedMessageRequest> paymentKafkaTemplate;

    @InjectMocks
    private KafkaEmailProducer kafkaEmailProducer;

    @BeforeEach
    void setUp() {

        kafkaEmailProducer = new KafkaEmailProducer(emailKafkaTemplate, paymentKafkaTemplate);

        TemporaryProductVO temporaryProductVO = new TemporaryProductVO(
                TestConstants.ID,
                TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION,
                TestConstants.PRODUCT_PRICE,
                TestConstants.PRODUCT_QUANTITY
        );

        emailVerificationMessageRequest = new EmailVerificationMessageRequest(EMAIL, TestConstants.USER_VERIFY_CODE);
        paymentApprovedMessageRequest = new PaymentApprovedMessageRequest(
                EMAIL, List.of(temporaryProductVO), TOTAL_PRICE
        );
    }

    @Test
    void testSendEmailVerificationCode_WhenSuccessful_ShouldSendToKafka() {
        kafkaEmailProducer.sendEmailVerificationCode(emailVerificationMessageRequest);
        verify(emailKafkaTemplate, times(1)).send(KAFKA_EMAIL_TOPIC, emailVerificationMessageRequest);
    }

    @Test
    void testSendPaymentApprovedMessage_WhenSuccessful_ShouldSendToKafka() {
        kafkaEmailProducer.sendPaymentApprovedMessage(paymentApprovedMessageRequest);
        verify(paymentKafkaTemplate, times(1)).send(KAFKA_PAYMENT_APPROVED_TOPIC, paymentApprovedMessageRequest);
    }
}
