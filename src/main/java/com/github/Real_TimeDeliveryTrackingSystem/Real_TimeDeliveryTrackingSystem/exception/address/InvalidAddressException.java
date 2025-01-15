package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAddressException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_ADDRESS_MESSAGE;

    public InvalidAddressException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
