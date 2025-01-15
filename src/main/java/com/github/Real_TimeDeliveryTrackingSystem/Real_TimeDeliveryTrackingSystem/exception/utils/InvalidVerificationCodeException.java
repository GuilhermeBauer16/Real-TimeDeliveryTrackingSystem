package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidVerificationCodeException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_VERIFICATION_CODE_MESSAGE;

    public InvalidVerificationCodeException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
