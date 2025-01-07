package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Controller for handling user verification and verification code regeneration requests.
 * <p>
 * This controller contains endpoints for regenerating a verification code for a user and verifying a code submitted by the user.
 * These endpoints are used for email verification during user authentication.
 * </p>
 */

public interface VerificationCodeControllerContract {

    /**
     * Regenerates a verification code for the user with the specified email address and sends it via email.
     * <p>
     * This method is typically used when a user needs a new verification code (e.g., after the original code expired).
     * The new verification code will be sent to the user's email address.
     * </p>
     *
     * @param email the email address of the user for whom the verification code will be regenerated
     * @return a {@link ResponseEntity} with status {@code 200 OK} if the code was successfully regenerated, or an appropriate error status
     * @throws MessagingException if an error occurs while sending the email
     */

    @PostMapping("/regenerateCode/{email}")
    @Operation(summary = "Regenerate verification code",
            description = "Regenerate verification code by user when they pass the email",
            tags = "Email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Will throw User Already Authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw User Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> regenerateVerificationCode(@PathVariable("email") String email) throws MessagingException;

    /**
     * Verifies the provided verification code submitted by the user.
     * <p>
     * This method checks the validity of the code provided by the user. If the code is correct and not expired, the user's
     * authentication status will be updated accordingly. If the code is invalid or expired, an exception will be thrown.
     * </p>
     *
     * @param request the request containing the user's email and the verification code to be verified
     * @return a {@link ResponseEntity} with status {@code 200 OK} if the verification is successful, or an appropriate error status
     * @throws MessagingException if an error occurs while processing the request (if applicable)
     */

    @PostMapping("/verify")
    @Operation(summary = "Verify the verification code",
            description = "Verify the verification code when the user pass the email and the verification code",
            tags = "Email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Will throw User Already Authenticated," +
                    "Expired Verification Code, or Invalid Verification Code Exception",
                    content = @Content),

            @ApiResponse(responseCode = "404", description = "Will Throw User Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> verifyCode(@RequestBody VerificationCodeRequest request) throws MessagingException;

}
