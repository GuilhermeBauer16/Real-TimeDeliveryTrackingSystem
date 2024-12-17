package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.CUSTOMER_NOT_FOUND_MESSAGE;

    public CustomerNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
