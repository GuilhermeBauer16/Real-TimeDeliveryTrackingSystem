package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.ExpiredVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserNotAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.CodeValidatorService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeValidatorServiceTest {

    private static final String USER_ALREADY_AUTHENTICATED_MESSAGE = "This user is already authenticated.";
    private static final String USER_NOT_AUTHENTICATED_MESSAGE = "This user is not authenticated, please authenticate the user and try again.";
    private static final String EXPIRED_VERIFICATION_CODE_MESSAGE = "This verification code is expired.";
    private static final String INVALID_VERIFICATION_CODE_MESSAGE = "This verification code %s is not valid.";

    @Mock
    private UserService userService;

    @InjectMocks
    private CodeValidatorService codeValidatorService;

    private UserVO userVO;
    private VerificationCodeRequest verificationCodeRequest;

    private static final String EMAIL = "customer@example.com";
    private static final LocalDateTime CODE_EXPIRATION = LocalDateTime.now().plusDays(1);
    private static final LocalDateTime EXPIRED_CODE_EXPIRATION = LocalDateTime.now().minusDays(1);
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final boolean AUTHENTICATED = false;
    private static final String VERIFY_CODE = "574077";
    private static final String ANOTHER_VERIFY_CODE = "574037";

    @BeforeEach
    void setUp() {
        userVO = new UserVO(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME, VERIFY_CODE, AUTHENTICATED, CODE_EXPIRATION);
        verificationCodeRequest = new VerificationCodeRequest(EMAIL, VERIFY_CODE);
    }

    @Test
    void testEvaluatedVerifyCode_WhenUserIsAlreadyAuthenticated_ShouldThrowUserAlreadyAuthenticatedException() {

        userVO.setAuthenticated(true);
        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);

        UserAlreadyAuthenticatedException exception = assertThrows(UserAlreadyAuthenticatedException.class,
                () -> codeValidatorService.evaluatedVerifyCode(verificationCodeRequest));

        assertNotNull(exception);
        assertEquals(UserAlreadyAuthenticatedException.ERROR.formatErrorMessage(USER_ALREADY_AUTHENTICATED_MESSAGE), exception.getMessage());
    }


    @Test
    void testEvaluatedVerifyCode_WhenCodeIsInvalid_ShouldThrowInvalidVerificationCodeException() {

        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);
        verificationCodeRequest.setCode(ANOTHER_VERIFY_CODE);

        InvalidVerificationCodeException exception = assertThrows(InvalidVerificationCodeException.class,
                () -> codeValidatorService.evaluatedVerifyCode(verificationCodeRequest));

        assertNotNull(exception);
        assertEquals(String.format(InvalidVerificationCodeException.ERROR.
                formatErrorMessage(INVALID_VERIFICATION_CODE_MESSAGE), ANOTHER_VERIFY_CODE), exception.getMessage());
    }

    @Test
    void testEvaluatedVerifyCode_WhenValidCode_ShouldAuthenticateUser() {

        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);

        codeValidatorService.evaluatedVerifyCode(verificationCodeRequest);


        verify(userService, times(1)).updateUser(any());
        assertTrue(userVO.isAuthenticated());
    }

    @Test
    void testEvaluatedVerifyCode_WhenExpirationCodeIsExpired_ShouldThrowExpiredVerificationCodeException() {
        userVO.setCodeExpiration(EXPIRED_CODE_EXPIRATION);
        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);


        ExpiredVerificationCodeException exception = assertThrows(ExpiredVerificationCodeException.class,
                () -> codeValidatorService.evaluatedVerifyCode(verificationCodeRequest));
        assertNotNull(exception);
        assertEquals(ExpiredVerificationCodeException.ERROR.formatErrorMessage(EXPIRED_VERIFICATION_CODE_MESSAGE), exception.getMessage());
    }

    @Test
    void testVerifyIfUserIsAuthenticated_WhenUserIsNotAuthenticated_ShouldThrowUserNotAuthenticatedException() {

        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);


        UserNotAuthenticatedException exception = assertThrows(UserNotAuthenticatedException.class,
                () -> codeValidatorService.verifyIfUserIsAuthenticated(EMAIL));
        assertNotNull(exception);
        assertEquals(UserNotAuthenticatedException.ERROR.formatErrorMessage(USER_NOT_AUTHENTICATED_MESSAGE), exception.getMessage());
    }

    @Test
    void testVerifyIfUserIsAuthenticated_WhenUserIsAuthenticated_ShouldNotThrowException() {

        userVO.setAuthenticated(true);
        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);


        assertDoesNotThrow(() -> codeValidatorService.verifyIfUserIsAuthenticated(EMAIL));
    }


}
