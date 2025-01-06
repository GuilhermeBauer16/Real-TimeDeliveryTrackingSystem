package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerifyCodeRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.VerifyCodeValidatorService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailSenderService emailSenderService;
    private final VerifyCodeValidatorService verifyCodeValidatorService;

    public EmailController(EmailSenderService emailSenderService, VerifyCodeValidatorService verifyCodeValidatorService) {
        this.emailSenderService = emailSenderService;
        this.verifyCodeValidatorService = verifyCodeValidatorService;
    }

    @PostMapping("/{email}")
    public ResponseEntity<Void> sendEmail(@PathVariable("email")String email) throws MessagingException {
        emailSenderService.sendValidatorCodeToUser(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody VerifyCodeRequest request) throws MessagingException {
        verifyCodeValidatorService.evaluatedVerifyCode(request);
        return ResponseEntity.ok().build();
    }


}
