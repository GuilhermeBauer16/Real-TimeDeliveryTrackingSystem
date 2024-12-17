package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserCredentialsNotMatchedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.LoginRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.LoginResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.JwtDetailsService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.JwtTokenService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.LoginService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class LoginTest {

    private static final String BAD_CREDENTIALS_MESSAGE = "An user with that email: %s was not matched";

    private static final String EMAIL = "test@gmail.com";
    private static final String PASSWORD = "test";
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJST0xFUyI6IltST0xFX1VTRVJdIiwic3ViIjoiam9obmRv" +
            "ZUBleGFtcGxlMTIuY29tIiwiaWF0IjoxNzI5MTk0MzQ1LCJleHAiOjE3MjkxOTYxNDV9.dMP8-0uQ2K67qogZVTdf4KO2YGwMnER_YkGzOV7Xstc";

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private JwtDetailsService jwtDetailsService;

    @Mock
    private UserDetails userDetails;

    @Mock
    private LoginRequest loginRequest;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest(EMAIL, PASSWORD);
    }

    @Test
    void testLogin_WhenSuccess_ShouldReturnLoginResponse() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtDetailsService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtTokenService.generateToken(userDetails)).thenReturn(JWT_TOKEN);

        LoginResponse login = loginService.login(loginRequest);

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtDetailsService, times(1)).loadUserByUsername(EMAIL);
        verify(jwtTokenService, times(1)).generateToken(userDetails);

        assertNotNull(login);
        assertEquals(EMAIL, login.getEmail());
        assertEquals(JWT_TOKEN, login.getToken());
    }

    @Test
    void testLogin_WhenCredentialsNotMatch_ShouldThrowBadCredentialsException() {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException(String.format(BAD_CREDENTIALS_MESSAGE, EMAIL)));

        UserCredentialsNotMatchedException exception = assertThrows(UserCredentialsNotMatchedException.class, () -> loginService.login(loginRequest));

        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserCredentialsNotMatchedException
                .ERROR.formatErrorMessage(String.format(BAD_CREDENTIALS_MESSAGE, EMAIL)));

        verify(jwtDetailsService, never()).loadUserByUsername(anyString());
        verify(jwtTokenService, never()).generateToken(any(UserDetails.class));
    }

    }



