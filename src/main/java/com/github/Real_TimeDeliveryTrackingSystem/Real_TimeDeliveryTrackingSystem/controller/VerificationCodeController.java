package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.VerificationCodeControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.CodeValidatorService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verificationCode")
public class VerificationCodeController implements VerificationCodeControllerContract {

    private final EmailSenderService emailSenderService;
    private final CodeValidatorService verifyCodeValidatorService;

    public VerificationCodeController(EmailSenderService emailSenderService, CodeValidatorService verifyCodeValidatorService) {
        this.emailSenderService = emailSenderService;
        this.verifyCodeValidatorService = verifyCodeValidatorService;
    }

    @Override
    public ResponseEntity<Void> regenerateVerificationCode(String email) throws MessagingException {
        emailSenderService.sendEmailWithValidatorCodeToUser(email);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity<Void> verifyCode(VerificationCodeRequest request) {
        verifyCodeValidatorService.evaluatedVerifyCode(request);
        return ResponseEntity.ok().build();
    }


}
