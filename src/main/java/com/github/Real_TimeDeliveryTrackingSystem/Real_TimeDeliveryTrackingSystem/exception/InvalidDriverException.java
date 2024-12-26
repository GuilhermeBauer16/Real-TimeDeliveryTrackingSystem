package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDriverException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_CUSTOMER_MESSAGE;

    public InvalidDriverException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
