package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface VerificationCodeControllerContract {

    @PostMapping("/regenerateCode/{email}")
    ResponseEntity<Void> regenerateVerificationCode(@PathVariable("email") String email) throws MessagingException;

    @PostMapping("/verify")
    ResponseEntity<Void> verifyCode(@RequestBody VerificationCodeRequest request) throws MessagingException;

}
