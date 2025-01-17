package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationUtilsException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.EXCEPTION_TYPE_NOT_THROWN;

    public ValidationUtilsException(String message, Throwable cause) {
        super(ERROR.formatErrorMessage(message), cause);
    }
}
