package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contract for sending emails with validation codes to users.
 * <p>
 * This interface defines the method for sending validation codes via email, which is typically used for
 * verifying user email addresses during the authentication or registration process.
 * </p>
 * <p>
 * Implementing classes are responsible for:
 * <ul>
 *   <li>Generating a unique validation code.</li>
 *   <li>Sending the code to the user's email address.</li>
 *   <li>Handling email sending exceptions appropriately.</li>
 * </ul>
 * </p>
 */
public interface EmailSendServiceContract {

    /**
     * Sends an email with a validation code to the specified user's email address.
     * <p>
     * This method generates a validation code and sends it via email using an HTML template.
     * It may throw a {@link MessagingException} if there is an issue during the email-sending process.
     * </p>
     *
     * @param email the email address of the user to whom the validation code will be sent
     * @throws MessagingException                if an error occurs while sending the email
     * @throws IllegalArgumentException          if the provided email address is invalid
     * @throws UserAlreadyAuthenticatedException if the user is already authenticated.
     * @see JavaMailSender
     * @see MimeMessageHelper
     * @see MimeMessage
     * @see SpringTemplateEngine
     * @see Context
     */

    void sendEmailWithValidatorCodeToUser(String email) throws MessagingException;

    void sendMailToApprovedPayment(String to, List<TemporaryProductVO> temporaryProductVOS, BigDecimal totalPrice) throws MessagingException;
}
