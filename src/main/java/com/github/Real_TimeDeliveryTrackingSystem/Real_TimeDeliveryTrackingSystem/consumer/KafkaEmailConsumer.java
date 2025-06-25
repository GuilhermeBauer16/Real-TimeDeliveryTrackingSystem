package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEmailConsumer {


    private final EmailSenderService emailSenderService;

    @Autowired
    public KafkaEmailConsumer(EmailSenderService emailSenderService) {

        this.emailSenderService = emailSenderService;
    }

    @KafkaListener(topics = "email-verification-topic", groupId = "email-service-group",
            containerFactory = "emailVerificationKafkaListenerContainerFactory")
    public void listenEmailVerification(EmailVerificationMessageRequest emailVerificationMessageRequest) {
        System.out.println(emailVerificationMessageRequest);

        try {
            emailSenderService.sendValidatorCode(emailVerificationMessageRequest.getEmail(), emailVerificationMessageRequest.getCode());
            System.out.println("Payment approved email sent successfully to: " + emailVerificationMessageRequest.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "payment-approved-topic", groupId = "email-service-group",
            containerFactory = "PaymentApprovedKafkaListenerContainerFactory")
    public void listenPaymentApproved(PaymentApprovedMessageRequest message) {
        System.out.println("Received payment approved message: " + message);
        try {

            emailSenderService.sendMailToApprovedPayment(message.getRecipientEmail(), message.getTemporaryProductVOList(), message.getTotalAmount());
            System.out.println("Payment approved email sent successfully to: " + message.getRecipientEmail());
        } catch (Exception e) {
            System.err.println("Failed to send payment approved email to " + message.getRecipientEmail() + ": " + e.getMessage());

        }
    }
}
