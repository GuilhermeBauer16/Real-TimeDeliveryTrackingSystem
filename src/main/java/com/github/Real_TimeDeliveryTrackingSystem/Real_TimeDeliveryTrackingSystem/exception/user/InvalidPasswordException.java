package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_PASSWORD_MESSAGE;

    public InvalidPasswordException(String message) {
        super(ERROR.formatErrorMessage(message));
    }

}
