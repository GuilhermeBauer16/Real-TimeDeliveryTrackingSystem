package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.ExpiredVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.InvalidVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.CodeValidatorService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final boolean AUTHENTICATED = false;


    @BeforeEach
    void setUp() {
        userVO = new UserVO(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME, TestConstants.USER_VERIFY_CODE,
                AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);

        verificationCodeRequest = new VerificationCodeRequest(EMAIL, TestConstants.USER_VERIFY_CODE);
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
        verificationCodeRequest.setCode(TestConstants.USER_UPDATED_VERIFY_CODE);

        InvalidVerificationCodeException exception = assertThrows(InvalidVerificationCodeException.class,
                () -> codeValidatorService.evaluatedVerifyCode(verificationCodeRequest));

        assertNotNull(exception);
        assertEquals(String.format(InvalidVerificationCodeException.ERROR.
                formatErrorMessage(INVALID_VERIFICATION_CODE_MESSAGE), TestConstants.USER_UPDATED_VERIFY_CODE), exception.getMessage());
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
        userVO.setCodeExpiration(TestConstants.USER_EXPIRED_CODE_EXPIRATION);
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
