package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatedLicensePlateException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.DUPLICATED_LICENSE_PLATE_MESSAGE;

    public DuplicatedLicensePlateException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
