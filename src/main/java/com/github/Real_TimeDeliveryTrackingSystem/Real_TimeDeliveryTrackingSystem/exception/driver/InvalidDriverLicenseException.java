package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDriverLicenseException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_DRIVER_LICENSE_MESSAGE;

    public InvalidDriverLicenseException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
