package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer.KafkaEmailProducer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.UserUpdateRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import constants.TestConstants;
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

import java.math.BigDecimal;
import java.util.List;

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
class EmailSenderServiceTest {

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

    @Mock
    private KafkaEmailProducer kafkaEmailProducer;

    private TemporaryProductVO temporaryProductVO;

    @InjectMocks
    private EmailSenderService emailSenderService;


    private static final String EMAIL = "customerAddress@example.com";
    private static final String PROCESSED_MAIL = "processed-email-content";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_CUSTOMER;
    private static final boolean AUTHENTICATED = false;


    @BeforeEach
    void setUp() {
        userVO = new UserVO(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME, TestConstants.USER_VERIFY_CODE,
                AUTHENTICATED, TestConstants.USER_CODE_EXPIRATION);

        temporaryProductVO = new TemporaryProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);
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
    void testSendEmailWithValidatorCodeToUser_WhenUserIsNotAuthenticated_ShouldSendEmail() {

        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);

        emailSenderService.sendEmailWithValidatorCodeToUser(EMAIL);

        verify(userService, times(1)).findUserByEmail(EMAIL);
        verify(kafkaEmailProducer, times(1)).sendEmailVerificationCode(any(EmailVerificationMessageRequest.class));
        verify(userService, times(1)).updateUser(any(UserUpdateRequest.class));


    }

    @Test
    void sendMailToApprovedPayment_WhenPaymentIsApproved_ShouldSendEmail() throws MessagingException {

        when(userService.findUserByEmail(EMAIL)).thenReturn(userVO);
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        when(templateEngine.process(anyString(), any())).thenReturn(PROCESSED_MAIL);

        emailSenderService.sendMailToApprovedPayment(EMAIL, List.of(temporaryProductVO), new BigDecimal(TestConstants.SHOPPING_CART_TOTAL_PRICE));


        verify(javaMailSender, times(1)).send(any(MimeMessage.class));

    }
}
