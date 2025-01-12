package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.UserUpdateRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.EmailSendServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.CodeGeneratorUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;

@Service
public class EmailSenderService implements EmailSendServiceContract {

    private static final String USER_ALREADY_AUTHENTICATED_MESSAGE = "This user is already authenticated.";
    private static final String EMAIL_TEMPLATE_PATH = "email/verification-email";
    private static final String SUBJECT = "Verification of the Email";
    private static final String CHARSET = "UTF-8";
    private static final String CONTEXT_CODE_VARIABLE = "code";
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRATION_TIME = 30;


    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserService userService;


    @Autowired
    public EmailSenderService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, UserService userService) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    private void sendValidatorCode(String to, String code) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        CHARSET);

        Context context = new Context();
        context.setVariable(CONTEXT_CODE_VARIABLE, code);


        String html = templateEngine.process(EMAIL_TEMPLATE_PATH, context);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(SUBJECT);
        mailSender.send(mimeMessage);

    }

    @Override
    public void sendEmailWithValidatorCodeToUser(String email) throws MessagingException {

        UserVO userByEmail = userService.findUserByEmail(email);

        if (userByEmail.isAuthenticated()) {
            throw new UserAlreadyAuthenticatedException(USER_ALREADY_AUTHENTICATED_MESSAGE);

        }
        String code = CodeGeneratorUtils.generateCode(CODE_LENGTH);
        sendValidatorCode(email, code);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(email, code, false, LocalDateTime.now().plusMinutes(30));
        userService.updateUser(userUpdateRequest);


    }


}
