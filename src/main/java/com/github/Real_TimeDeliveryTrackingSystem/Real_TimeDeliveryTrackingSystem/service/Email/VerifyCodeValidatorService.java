package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.Email;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerifyCodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerifyCodeValidatorService {

    private final UserRepository userRepository;

    @Autowired
    public VerifyCodeValidatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void evaluatedVerifyCode(VerifyCodeRequest verifyCodeRequest) {
        // Fetch the user by email
        UserEntity userEntity = userRepository.findUserByEmail(verifyCodeRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User with email " + verifyCodeRequest.getEmail() + " does not exist."));

        // Check if the user is already authenticated
        if (userEntity.isAuthenticated()) {
            throw new RuntimeException("User is already authenticated.");
        }

        // Check if the verification code has expired
        if (LocalDateTime.now().isAfter(userEntity.getCodeExpiration())) {
            throw new RuntimeException("The verification code has expired.");
        }

        // Check if the verification code matches
        if (!userEntity.getVerifyCode().strip().equals(verifyCodeRequest.getCode())) {
            throw new RuntimeException("Invalid verification code provided.");
        }

        // Update user status to authenticated
        userEntity.setAuthenticated(true);
        userRepository.save(userEntity);
    }

    public void verifyIfUserIsAuthenticated(String email) {
        // Fetch the user by email
        UserEntity userEntity = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " does not exist."));

        // Check if the user is not authenticated
        if (!userEntity.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated.");
        }
    }
}
