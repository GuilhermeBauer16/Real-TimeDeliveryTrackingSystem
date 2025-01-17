package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LicensePlateNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.LICENSE_PLATE_NOT_FOUND_MESSAGE;

    public LicensePlateNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
