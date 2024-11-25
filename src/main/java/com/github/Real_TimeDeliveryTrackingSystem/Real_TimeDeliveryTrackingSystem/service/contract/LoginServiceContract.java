package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Service interface for managing user login within the application.
 *
 * <p>This service provides methods to log in a user based on specific
 * criteria.
 */

public interface LoginServiceContract {

    /**
     * Authenticates a user based on provided login credentials.
     *
     * <p>This method verifies the user credentials in the {@link LoginRequest} object.
     * If the credentials are valid, it generates a login response containing user information
     * and a JWT token.
     *
     * @param loginRequest The {@link LoginRequest} containing the user's login details,
     *                     including username/email and password.
     * @return {@link LoginResponse} object containing the user's authenticated session details,
     *         including a JWT token if authentication is successful.
     *
     * @throws UsernameNotFoundException if the login credentials are invalid.
     * @throws BadCredentialsException if the credentials authenticated are invalid.
     * @see LoginResponse
     * @see LoginRequest
     */

    LoginResponse login(LoginRequest loginRequest);
}
