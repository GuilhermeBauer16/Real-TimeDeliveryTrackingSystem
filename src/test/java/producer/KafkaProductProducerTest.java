package producer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer.KafkaProductProducer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;
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
class KafkaProductProducerTest {

    private static final String EMAIL = "user@example.com";
    private static final BigDecimal TRANSITION_AMOUNT = BigDecimal.valueOf(TestConstants.SHOPPING_CART_TOTAL_PRICE);
    private static final String KAFKA_PRODUCT_TOPIC = "product-topic";

    private PaymentProcessedRequest paymentProcessedRequest;

    @Mock
    private KafkaTemplate<String, PaymentProcessedRequest> paymentApprovedKafkaTemplate;

    @InjectMocks
    private KafkaProductProducer kafkaProductProducer;

    @BeforeEach
    void setUp() {

        kafkaProductProducer = new KafkaProductProducer(paymentApprovedKafkaTemplate);
        paymentProcessedRequest = new PaymentProcessedRequest(TestConstants.ID, EMAIL, TRANSITION_AMOUNT, List.of(TestConstants.ID, TestConstants.ID));
    }

    @Test
    void testSendProductMessage_WhenSuccessful_ShouldSendToKafka() {
        kafkaProductProducer.sendProductMessage(paymentProcessedRequest);
        verify(paymentApprovedKafkaTemplate, times(1)).send(KAFKA_PRODUCT_TOPIC, paymentProcessedRequest);
    }

}
