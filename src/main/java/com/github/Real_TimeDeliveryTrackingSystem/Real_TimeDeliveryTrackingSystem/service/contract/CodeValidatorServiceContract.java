package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.ExpiredVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.InvalidVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotAuthenticatedException;


/**
 * Contract for validating user verification codes during authentication.
 * <p>
 * This interface defines methods for evaluating verification codes and checking if a user is already authenticated.
 * The implementing class is responsible for processing verification code requests, ensuring that users are not already authenticated,
 * and validating the expiration and correctness of the provided verification codes.
 * </p>
 */

public interface CodeValidatorServiceContract {

    /**
     * Evaluates the provided verification code and updates the user's authentication status.
     * <p>
     * This method checks if the verification code provided by the user is valid, not expired, and corresponds to the user.
     * If the code is valid and the user is authenticated successfully, the user's status is updated.
     * If any issue arises (e.g., expired code, invalid code, user already authenticated), an exception will be thrown.
     * </p>
     *
     * @param verifyCodeRequest the request containing the user's email and the verification code to be evaluated
     * @throws ExpiredVerificationCodeException if the verification code has expired
     * @throws InvalidVerificationCodeException if the provided code is invalid
     * @throws UserAlreadyAuthenticatedException if the user is already authenticated
     */

    void evaluatedVerifyCode(VerificationCodeRequest verifyCodeRequest);

    /**
     * Verifies if a user is already authenticated based on their email.
     * <p>
     * This method checks the authentication status of the user. If the user is not authenticated,
     * a {@link UserNotAuthenticatedException} will be thrown.
     * </p>
     *
     * @param email the email address of the user to check
     * @throws UserNotAuthenticatedException if the user is not authenticated
     */

    void verifyIfUserIsAuthenticated(String email);


}
