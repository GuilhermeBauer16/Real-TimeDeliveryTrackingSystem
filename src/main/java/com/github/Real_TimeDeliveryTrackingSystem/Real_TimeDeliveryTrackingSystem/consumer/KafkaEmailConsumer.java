package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailConsumer {

    private static final String KAFKA_EMAIL_TOPIC = "email-verification-topic";
    private static final String KAFKA_EMAIL_CONTAINER_FACTORY = "emailVerificationKafkaListenerContainerFactory";
    private static final String KAFKA_PAYMENT_APPROVED_TOPIC = "payment-approved-topic";
    private static final String KAFKA_PAYMENT_APPROVED_CONTAINER_FACTORY = "paymentApprovedKafkaListenerContainerFactory";
    private static final String KAFKA_EMAIL_GROUP_ID = "email-service-group";
    private final EmailSenderService emailSenderService;

    @Autowired
    public KafkaEmailConsumer(EmailSenderService emailSenderService) {

        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = KAFKA_EMAIL_TOPIC, groupId = KAFKA_EMAIL_GROUP_ID,
            containerFactory = KAFKA_EMAIL_CONTAINER_FACTORY)
    public void listenEmailVerification(EmailVerificationMessageRequest emailVerificationMessageRequest) throws MessagingException {

        emailSenderService.sendValidatorCode(emailVerificationMessageRequest.getEmail(), emailVerificationMessageRequest.getCode());


    }

    @KafkaListener(topics = KAFKA_PAYMENT_APPROVED_TOPIC, groupId = KAFKA_EMAIL_GROUP_ID,
            containerFactory = KAFKA_PAYMENT_APPROVED_CONTAINER_FACTORY)
    public void listenPaymentApproved(PaymentApprovedMessageRequest paymentApprovedMessageRequest) throws MessagingException {

        emailSenderService.sendMailToApprovedPayment(paymentApprovedMessageRequest.getRecipientEmail()
                , paymentApprovedMessageRequest.getTemporaryProductVOList(), paymentApprovedMessageRequest.getTotalAmount());


    }
}
