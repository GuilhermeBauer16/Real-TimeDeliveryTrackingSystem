package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPhoneNumberException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberValidator {

    private static final String PHONE_REGEX = "\\+?[0-9]+";
    private static final String PHONE_NULL_OR_EMPTY_MESSAGE = "The phone number cannot be null or empty";
    private static final String INVALID_PHONE_MESSAGE = "The phone number %s is invalid. Please verify the fields and try again.";
    private static final String INVALID_PHONE_CHARACTERS_MESSAGE = "Phone number %s contains invalid characters!";
    private static final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

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
            throw new InvalidPhoneNumberException(String.format(INVALID_PHONE_MESSAGE, phoneNumber));
        }
    }
}