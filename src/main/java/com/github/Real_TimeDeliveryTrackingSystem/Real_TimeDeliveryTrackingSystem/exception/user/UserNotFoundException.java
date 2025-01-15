package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.USER_NOT_FOUND_MESSAGE;

    public UserNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
