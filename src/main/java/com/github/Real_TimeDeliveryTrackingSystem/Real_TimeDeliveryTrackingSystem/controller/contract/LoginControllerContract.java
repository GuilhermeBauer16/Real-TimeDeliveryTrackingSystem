package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * API endpoint contract for user login operations.
 *
 * <p>This controller provides an endpoint for authenticating users.
 * The client must send a {@link LoginRequest} containing user credentials,
 * and if successful, receives a {@link LoginResponse} containing the authentication details
 * including a JWT token.
 */

public interface LoginControllerContract {

    /**
     * Authenticates a user based on provided login credentials.
     *
     * <p>This endpoint accepts a JSON payload containing the user's credentials,
     * such as username/email and password. On successful authentication,
     * it returns a {@link LoginResponse} with a JWT token.
     *
     * @param request The {@link LoginRequest} object containing the user's login credentials.
     * @return {@link ResponseEntity} containing the {@link LoginResponse} with a JWT token
     *         if authentication is successful, or an appropriate error response otherwise.
     * @see LoginResponse
     * @see LoginRequest
     * @see LoginService
     */

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Do a login",
            description = "Do a login and return an JWT token if was success",
            tags = "login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401", description = "Will throw BadCredentialsException",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);
}
