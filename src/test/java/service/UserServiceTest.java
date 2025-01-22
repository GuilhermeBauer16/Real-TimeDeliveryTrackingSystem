package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.UserUpdateRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final String USER_NOT_FOUND_MESSAGE = "User was not found!, Please verify and try again.";

    private static final String EMAIL = "user@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;

    private UserUpdateRequest userUpdateRequest;


    @Mock
    private UserRepository userRepository;


    @Mock
    private UserEntity userEntity;


    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {

        userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME, TestConstants.USER_VERIFY_CODE,
                TestConstants.USER_AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);
        userUpdateRequest = new UserUpdateRequest(EMAIL, TestConstants.USER_VERIFY_CODE,
                TestConstants.USER_AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);

    }

    @Test
    void testFindUseByEmail_WhenUserWasFound_ShouldReturnUserVOObject() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userEntity));

        UserVO user = userService.findUserByEmail(EMAIL);


        verify(userRepository, times(1)).findUserByEmail(any());

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(TestConstants.ID, user.getId());
        assertEquals(TestConstants.USER_USERNAME, user.getName());
        assertEquals(TestConstants.USER_PASSWORD, user.getPassword());
        assertEquals(ROLE_NAME, user.getUserProfile());
        assertEquals(TestConstants.USER_AUTHENTICATED, user.isAuthenticated());
        assertEquals(TestConstants.USER_VERIFY_CODE, user.getVerifyCode());
        assertEquals(TestConstants.USER_CODE_EXPIRATION, user.getCodeExpiration());


    }

    @Test
    void testFindUseByEmail_WhenUserNotFound_ShouldThrowUserNotFoundException() {

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail(EMAIL));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(USER_NOT_FOUND_MESSAGE));

    }

    @Test
    void testUpdateUser_WhenSuccess_ShouldReturnUpdatedUserObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(), any(), anyString(), any())).thenAnswer(invocation -> userEntity);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any())).thenAnswer(invocation -> null);

            when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userEntity));
            when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);


            UserVO user = userService.updateUser(userUpdateRequest);
            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any(), any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            verify(userRepository, times(1)).findUserByEmail(any());
            verify(userRepository, times(1)).save(any(UserEntity.class));


            assertNotNull(user);
            assertNotNull(user.getId());
            assertEquals(EMAIL, user.getEmail());
            assertEquals(TestConstants.ID, user.getId());
            assertEquals(TestConstants.USER_USERNAME, user.getName());
            assertEquals(TestConstants.USER_PASSWORD, user.getPassword());
            assertEquals(ROLE_NAME, user.getUserProfile());
            assertEquals(TestConstants.USER_AUTHENTICATED, user.isAuthenticated());
            assertEquals(TestConstants.USER_VERIFY_CODE, user.getVerifyCode());
            assertEquals(TestConstants.USER_CODE_EXPIRATION, user.getCodeExpiration());


        }

    }

    @Test
    void testUpdateUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.updateUser(userUpdateRequest));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(USER_NOT_FOUND_MESSAGE));

    }




}
