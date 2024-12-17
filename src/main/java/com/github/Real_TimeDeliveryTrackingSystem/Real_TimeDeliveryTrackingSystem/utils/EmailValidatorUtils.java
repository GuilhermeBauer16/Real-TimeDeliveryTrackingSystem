package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidEmailPatternException;

import java.util.regex.Pattern;
/**
 * Utility class for email validation.
 * <p>
 * This class provides a method to validate the format of an email address using a regular expression.
 * If the email does not match the specified pattern or is null/empty, an {@link InvalidEmailPatternException}
 * is thrown.
 * </p>
 *
 * <h3>Usage Example:</h3>
 * <pre>{@code
 * try {
 *     EmailValidatorUtils.verifyEmailPattern("example@domain.com");
 * } catch (InvalidEmailPatternException e) {
 *     System.out.println(e.getMessage());
 * }
 * }</pre>
 */
public class EmailValidatorUtils {

    private static final String INVALID_EMAIL_PATTERN_EXCEPTION_MESSAGE = "This email pattern is invalid, please verify if matches with that example" +
            " exampleEmail@Domain.com";

    private static final String EMAIL_NOT_BE_NULL_OR_EMPTY_MESSAGE = "The email can not be null or empty!";

    /**
     * Regular expression for validating an email address.
     * <p>
     * The pattern matches valid email addresses with the following rules:
     * <ul>
     *   <li>Starts with alphanumeric characters or valid special characters (._%+-).</li>
     *   <li>Followed by an "@" symbol and a valid domain name.</li>
     *   <li>The domain name must contain at least one dot, followed by 2 or more letters (e.g., .com, .org).</li>
     * </ul>
     * </p>
     */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Validates the format of an email address.
     * <p>
     * This method checks whether the given email address is valid based on a regular expression pattern.
     * If the email is null, empty, or does not match the pattern, it throws an {@link InvalidEmailPatternException}.
     * </p>
     *
     * @param email the email address to validate
     * @throws InvalidEmailPatternException if the email is null, empty, or does not match the required pattern
     */
    public static void verifyEmailPattern(String email) {

        if (email == null || email.isBlank()) {
            throw new InvalidEmailPatternException(EMAIL_NOT_BE_NULL_OR_EMPTY_MESSAGE);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidEmailPatternException(INVALID_EMAIL_PATTERN_EXCEPTION_MESSAGE);

        }


    }
}


