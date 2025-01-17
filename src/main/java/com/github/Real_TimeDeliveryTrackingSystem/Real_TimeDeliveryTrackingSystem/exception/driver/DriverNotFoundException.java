package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DriverNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.DRIVER_NOT_FOUND_MESSAGE;

    public DriverNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
