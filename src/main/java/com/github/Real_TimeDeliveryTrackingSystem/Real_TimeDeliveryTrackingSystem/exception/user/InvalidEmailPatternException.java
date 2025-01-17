package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEmailPatternException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_EMAIL_PATTERN_MESSAGE;

    public InvalidEmailPatternException(String message) {
        super(ERROR.formatErrorMessage(message));
    }

}
