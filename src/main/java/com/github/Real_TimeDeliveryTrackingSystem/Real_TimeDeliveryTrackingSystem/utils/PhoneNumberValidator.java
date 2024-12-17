package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPhoneNumberException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.SneakyThrows;


/**
 * Utility class for phone number validation.
 * <p>
 * This class validates phone numbers using Google's {@link PhoneNumberUtil} library.
 * It ensures the provided phone numbers are not null, empty, or invalid, and conform to a valid phone number format.
 * </p>
 *
 * <p> Validation steps include:
 * <ul>
 *   <li>Checking for null or empty values.</li>
 *   <li>Ensuring the phone number contains only valid characters (digits and an optional '+').</li>
 *   <li>Using {@link PhoneNumberUtil} to parse and validate the phone number.</li>
 * </ul>
 * </p>
 *
 * <p> If a phone number is invalid, this utility throws {@link InvalidPhoneNumberException} with an appropriate message. </p>

 */
public class PhoneNumberValidator {

    /**
     * Regular expression to validate a phone number format.
     * Allows an optional '+' at the beginning, followed by digits.
     */
    private static final String PHONE_REGEX = "\\+?[0-9]+";
    private static final String PHONE_NULL_OR_EMPTY_MESSAGE = "The phone number cannot be null or empty";
    private static final String INVALID_PHONE_MESSAGE = "The phone number %s is invalid. Please verify the fields and try again.";
    private static final String INVALID_PHONE_CHARACTERS_MESSAGE = "Phone number %s contains invalid characters!";

    /**
     * Instance of {@link PhoneNumberUtil} to handle phone number parsing and validation.
     */
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    /**
     * Validates a given phone number.
     * <p>
     * The method performs the following checks:
     * <ul>
     *   <li>Checks if the phone number is null or empty.</li>
     *   <li>Verifies that the phone number contains only digits and an optional leading '+'.</li>
     *   <li>Uses {@link PhoneNumberUtil} to parse and validate the phone number.</li>
     * </ul>
     * </p>
     *
     * @param phoneNumber The phone number to validate as a {@link String}.
     * @throws InvalidPhoneNumberException if the phone number is null, empty, contains invalid characters,
     *                                     or does not conform to a valid phone number format.
     * @throws NumberParseException        if the phone number cannot be parsed.
     */
    @SneakyThrows
    public static void validatePhoneNumber(String phoneNumber) {

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new InvalidPhoneNumberException(PHONE_NULL_OR_EMPTY_MESSAGE);
        }

        if (!phoneNumber.matches(PHONE_REGEX)) {
            throw new InvalidPhoneNumberException(String.format(INVALID_PHONE_CHARACTERS_MESSAGE, phoneNumber));
        }


        try {

            Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(phoneNumber, null);

            if (!phoneNumberUtil.isPossibleNumber(parsedNumber) || !phoneNumberUtil.isValidNumber(parsedNumber)) {
                throw new InvalidPhoneNumberException(String.format(INVALID_PHONE_MESSAGE, phoneNumber));
            }

        } catch (NumberParseException e) {
            throw new NumberParseException(e.getErrorType(),String.format(INVALID_PHONE_MESSAGE, phoneNumber));
        }


    }
}