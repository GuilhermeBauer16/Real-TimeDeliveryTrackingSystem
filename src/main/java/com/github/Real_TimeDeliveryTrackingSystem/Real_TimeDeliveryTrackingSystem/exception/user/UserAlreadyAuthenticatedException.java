package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyAuthenticatedException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.USER_ALREADY_AUTHENTICATED_MESSAGE;

    public UserAlreadyAuthenticatedException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
