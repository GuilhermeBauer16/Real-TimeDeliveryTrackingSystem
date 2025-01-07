package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.EmailAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserRegistrationService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class UserRegistrationTest {

    private static final String USER_NOT_FOUND_MESSAGE = "That User was not found";
    private static final String EMAIL_ALREADY_REGISTER_MESSAGE = "The email %s is already registered for another user!";

    private static final String EMAIL = "user@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;


    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserVO userVO;



    @Mock
    private UserEntity userEntity;


    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @BeforeEach
    void setUp() {

        userVO = new UserVO(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);
        userEntity = new UserEntity(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME);

    }

    @Test
    void testCreateUser_WhenSuccess_ShouldReturnUserObject() throws MessagingException {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class))).thenAnswer(invocation -> null);

            when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
            when(passwordEncoder.encode(anyString())).thenReturn(PASSWORD);


            UserVO user = userRegistrationService.createUser(userVO);

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any(Class.class)));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any(Class.class)));

            verify(userRepository, times(1)).save(any(UserEntity.class));
            verify(passwordEncoder, times(1)).encode(anyString());

            assertNotNull(user);
            assertNotNull(user.getId());
            assertEquals(EMAIL, user.getEmail());
            assertEquals(ID, user.getId());
            assertEquals(USERNAME, user.getName());


        }

    }

    @Test
    void testCreateUser_WhenUserNotFound_ShouldThrowUserNotFoundException() {

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userRegistrationService.createUser(null));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), UserNotFoundException.ERROR.formatErrorMessage(USER_NOT_FOUND_MESSAGE));

    }

    @Test
    void testCreate_WhenEmailAlreadyRegister_ShouldThrowEmailAlreadyRegisterException() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(userEntity));
        EmailAllReadyRegisterException exception = assertThrows(EmailAllReadyRegisterException.class, () ->
                userRegistrationService.createUser(userVO));

        assertNotNull(exception);
        assertEquals(exception.getMessage(),
                EmailAllReadyRegisterException.ERROR.formatErrorMessage(String.format(EMAIL_ALREADY_REGISTER_MESSAGE, EMAIL)));
    }



}
