package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.producer.KafkaEmailProducer;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.EmailVerificationMessageRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentApprovedMessageRequest;
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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class EmailSenderService implements EmailSendServiceContract {

    private static final String USER_ALREADY_AUTHENTICATED_MESSAGE = "This user is already authenticated.";
    private static final String EMAIL_TEMPLATE_PATH = "email/verification-email";
    private static final String SUBJECT = "Verification of the Email";
    private static final String CHARSET = "UTF-8";
    private static final String CONTEXT_CODE_VARIABLE = "code";
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRATION_TIME = 30;

    private static final String LOCALE_LANGUAGE = "pt";
    private static final String LOCALE_COUNTRY = "BR";
    private static final String PAYMENT_TEMPLATE_PATH = "email/payment-approved";
    private static final String PAYMENT_SUBJECT = "Payment Approved";
    private static final String CONTEXT_BUYER_NAME_VARIABLE = "buyerName";
    private static final String CONTEXT_ITEMS_VARIABLE = "items";
    private static final String CONTEXT_TOTAL_VARIABLE = "total";


    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserService userService;
    private final KafkaEmailProducer kafkaEmailProducer;


    @Autowired
    public EmailSenderService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, UserService userService, KafkaEmailProducer kafkaEmailProducer) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userService = userService;
        this.kafkaEmailProducer = kafkaEmailProducer;
    }

    public void sendValidatorCode(String to, String code) throws MessagingException {

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
        EmailVerificationMessageRequest emailVerificationMessageRequest = new EmailVerificationMessageRequest(email, code);
        kafkaEmailProducer.sendEmailVerificationCode(emailVerificationMessageRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(email, code, false, LocalDateTime.now().plusMinutes(EXPIRATION_TIME));
        userService.updateUser(userUpdateRequest);


    }

    @Override
    public void sendMailToApprovedPayment(String to, List<TemporaryProductVO> temporaryProductVOS, BigDecimal totalPrice) throws MessagingException {

        UserVO userByEmail = userService.findUserByEmail(to);

        Locale brazil = Locale.of(LOCALE_LANGUAGE, LOCALE_COUNTRY);
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(brazil);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);
        String formattedTotal = currencyFormat.format(totalPrice);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        CHARSET);

        Context context = new Context();
        context.setVariable(CONTEXT_BUYER_NAME_VARIABLE, userByEmail.getName());
        context.setVariable(CONTEXT_ITEMS_VARIABLE, temporaryProductVOS);
        context.setVariable(CONTEXT_TOTAL_VARIABLE, formattedTotal);


        String html = templateEngine.process(PAYMENT_TEMPLATE_PATH, context);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(PAYMENT_SUBJECT);
        mailSender.send(mimeMessage);

    }

    public void doSendMailToApprovedPayment(String to, List<TemporaryProductVO> temporaryProductVOS, BigDecimal totalPrice) {

        PaymentApprovedMessageRequest message = new PaymentApprovedMessageRequest(to, temporaryProductVOS, totalPrice);
        kafkaEmailProducer.sendPaymentApprovedMessage(message);
    }


}
