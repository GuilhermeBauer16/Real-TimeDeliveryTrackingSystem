package consumer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer.KafkaEmailConsumer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import constants.TestConstants;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaEmailConsumerTest {

    private static final String EMAIL = "user@example.com";
    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(TestConstants.SHOPPING_CART_TOTAL_PRICE);

    private EmailVerificationMessageRequest emailVerificationMessageRequest;
    private PaymentApprovedMessageRequest paymentApprovedMessageRequest;

    @Mock
    private EmailSenderService emailSenderService;

    @InjectMocks
    private KafkaEmailConsumer kafkaEmailConsumer;


    @BeforeEach
    void setUp() {

        TemporaryProductVO temporaryProductVO = new TemporaryProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        emailVerificationMessageRequest = new EmailVerificationMessageRequest(EMAIL, TestConstants.USER_VERIFY_CODE);
        paymentApprovedMessageRequest = new PaymentApprovedMessageRequest(EMAIL, List.of(temporaryProductVO), TOTAL_PRICE);
    }

    @Test
    void testListenPaymentApprovedMessage_WhenMessageIsReceived_ShouldSendMailToApprovedPayment() throws MessagingException {

        kafkaEmailConsumer.listenPaymentApproved(paymentApprovedMessageRequest);

        verify(emailSenderService, times(1)).sendMailToApprovedPayment(
                paymentApprovedMessageRequest.getRecipientEmail(),
                paymentApprovedMessageRequest.getTemporaryProductVOList(),
                paymentApprovedMessageRequest.getTotalAmount());
    }

    @Test
    void testListenEmailVerification_WhenMessageIsReceived_ShouldSendVerificationCode() throws MessagingException {

        kafkaEmailConsumer.listenEmailVerification(emailVerificationMessageRequest);

        verify(emailSenderService, times(1)).sendValidatorCode(
                emailVerificationMessageRequest.getEmail(),
                emailVerificationMessageRequest.getCode());
    }


}
