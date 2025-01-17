package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.ADDRESS_NOT_FOUND_MESSAGE;

    public AddressNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
