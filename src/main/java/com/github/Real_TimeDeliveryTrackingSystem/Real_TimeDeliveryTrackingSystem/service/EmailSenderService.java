package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.EmailSendServiceContract;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

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



}
