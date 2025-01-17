package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPhoneNumberException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_PHONE_NUMBER_MESSAGE;

    public InvalidPhoneNumberException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
