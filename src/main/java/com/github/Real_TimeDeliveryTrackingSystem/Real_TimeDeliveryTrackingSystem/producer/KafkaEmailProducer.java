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

    @Autowired
    public KafkaEmailProducer(KafkaTemplate<String, EmailVerificationMessageRequest> emailKafkaTemplate, KafkaTemplate<String, PaymentApprovedMessageRequest> paymentKafkaTemplate) {
        this.emailKafkaTemplate = emailKafkaTemplate;
        this.paymentKafkaTemplate = paymentKafkaTemplate;

    }

    public void sendEmailVerificationCode(EmailVerificationMessageRequest emailVerificationMessageRequest) {
        emailKafkaTemplate.send("email-verification-topic", emailVerificationMessageRequest);
        System.out.println("Email send!!!!");
    }

    public void sendPaymentApprovedMessage(PaymentApprovedMessageRequest paymentApprovedMessageRequest) {
        paymentKafkaTemplate.send("payment-approved-topic", paymentApprovedMessageRequest);
        System.out.println("Email send to a approved Payment!!!!");
    }
}
