package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.EmailSendServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.CodeGeneratorUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
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

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final UserRepository userRepository;

    private static final String EMAIL_TEMPLATE_PATH = "email/verification-email";
    private static final String SUBJECT = "Verification of the Email";
    private static final String CONTEXT_CODE_VARIABLE = "code";


    @Autowired
    public EmailSenderService(JavaMailSender mailSender, SpringTemplateEngine templateEngine, UserRepository userRepository) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userRepository = userRepository;
    }

    @Override
    public void sendValidatorCode(String to, String code) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        "UTF-8");

        Context context = new Context();
        context.setVariable(CONTEXT_CODE_VARIABLE, code);


        String html = templateEngine.process(EMAIL_TEMPLATE_PATH, context);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSubject(SUBJECT);
        mimeMessageHelper.setFrom("not-reply@gmail.com");
        mailSender.send(mimeMessage);

    }

    public void sendValidatorCodeToUser(String email) throws MessagingException {

        UserEntity userEntity = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));

        if(userEntity.isAuthenticated()){
            throw new RuntimeException("user is already authenticated");

        }
        String code = CodeGeneratorUtils.generateCode(6);
        sendValidatorCode(email,code);
        userEntity.setCodeExpiration(LocalDateTime.now().plusMinutes(30));
        userEntity.setVerifyCode(code);
        userRepository.save(userEntity);

    }



}
