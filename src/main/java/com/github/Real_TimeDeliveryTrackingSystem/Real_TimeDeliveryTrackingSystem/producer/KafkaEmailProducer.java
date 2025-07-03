package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEmailProducer {

    private final KafkaTemplate<String, EmailVerificationMessageRequest> emailKafkaTemplate;
    private final KafkaTemplate<String, PaymentApprovedMessageRequest> paymentKafkaTemplate;

    private static final String KAFKA_EMAIL_TOPIC = "email-verification-topic";
    private static final String KAFKA_PAYMENT_APPROVED_TOPIC = "payment-approved-topic";

    @Autowired
    public KafkaEmailProducer(KafkaTemplate<String, EmailVerificationMessageRequest> emailKafkaTemplate,
                              KafkaTemplate<String, PaymentApprovedMessageRequest> paymentKafkaTemplate) {
        this.emailKafkaTemplate = emailKafkaTemplate;
        this.paymentKafkaTemplate = paymentKafkaTemplate;

    }

    public void sendEmailVerificationCode(EmailVerificationMessageRequest emailVerificationMessageRequest) {
        emailKafkaTemplate.send(KAFKA_EMAIL_TOPIC, emailVerificationMessageRequest);
    }

    public void sendPaymentApprovedMessage(PaymentApprovedMessageRequest paymentApprovedMessageRequest) {
        paymentKafkaTemplate.send(KAFKA_PAYMENT_APPROVED_TOPIC, paymentApprovedMessageRequest);
    }
}
