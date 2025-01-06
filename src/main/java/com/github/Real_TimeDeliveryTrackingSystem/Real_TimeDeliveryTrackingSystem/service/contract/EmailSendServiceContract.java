package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import jakarta.mail.MessagingException;

public interface EmailSendServiceContract {

    void sendValidatorCode(String to, String code) throws MessagingException;
}
