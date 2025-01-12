package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserCredentialsNotMatchedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.CodeValidatorService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.LoginServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginServiceContract {

    private static final String BAD_CREDENTIALS_MESSAGE = "An user with that email: %s was not matched";

    private final JwtTokenService jwtTokenService;
    private final JwtDetailsService jwtDetailsService;
    private final AuthenticationManager authenticationManager;
    private final CodeValidatorService verifyCodeValidatorService;

    @Autowired
    public LoginService(JwtTokenService jwtTokenService, JwtDetailsService jwtDetailsService, AuthenticationManager authenticationManager, CodeValidatorService verifyCodeValidatorService) {
        this.jwtTokenService = jwtTokenService;
        this.jwtDetailsService = jwtDetailsService;
        this.authenticationManager = authenticationManager;
        this.verifyCodeValidatorService = verifyCodeValidatorService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        this.authenticate(loginRequest);
        UserDetails userDetails = this.jwtDetailsService.loadUserByUsername(loginRequest.getEmail());

        verifyCodeValidatorService.verifyIfUserIsAuthenticated(loginRequest.getEmail());
        final String token = jwtTokenService.generateToken(userDetails);

        return new LoginResponse(loginRequest.getEmail(), token);
    }

    private void authenticate(LoginRequest loginRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));


        }catch (BadCredentialsException e) {

            throw new UserCredentialsNotMatchedException(String.format(BAD_CREDENTIALS_MESSAGE, loginRequest.getEmail()));
        }
    }
}
