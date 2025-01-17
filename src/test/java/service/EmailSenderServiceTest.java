package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {

    private static final String USER_ALREADY_AUTHENTICATED_MESSAGE = "This user is already authenticated.";

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private SimpleMailMessage simpleMailMessage;

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private UserService userService;

    @Mock
    private UserVO userVO;

    @InjectMocks
    private EmailSenderService emailSenderService;

    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String EMAIL = "customerAddress@example.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final boolean AUTHENTICATED = false;
    private static final LocalDateTime CODE_EXPIRATION = LocalDateTime.now().plusDays(5);
    private static final String VERIFY_CODE = "574077";


    @BeforeEach
    void setUp() {
        userVO = new UserVO(ID, USERNAME, EMAIL, PASSWORD, ROLE_NAME, VERIFY_CODE, AUTHENTICATED, CODE_EXPIRATION);
    }

    @Test
    void testSendEmailWithValidatorCodeToUser_WhenUserIsAlreadyAuthenticated_ShouldThrowUserAlreadyAuthenticatedException() {
        userVO.setAuthenticated(true);
        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);

        UserAlreadyAuthenticatedException exception = assertThrows(UserAlreadyAuthenticatedException.class,
                () -> emailSenderService.sendEmailWithValidatorCodeToUser(EMAIL));

        assertNotNull(exception);
        assertEquals(UserAlreadyAuthenticatedException.ERROR.formatErrorMessage(USER_ALREADY_AUTHENTICATED_MESSAGE), exception.getMessage());
    }

    @Test
    void testSendEmailWithValidatorCodeToUser_WhenUserIsNotAuthenticated_ShouldSendEmail() throws MessagingException {

        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any())).thenReturn("processed-email-content"); // Mock email content


        emailSenderService.sendEmailWithValidatorCodeToUser(EMAIL);


        verify(javaMailSender, times(1)).send(any(MimeMessage.class));

    }
}
