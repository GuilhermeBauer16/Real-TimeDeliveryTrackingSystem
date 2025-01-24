package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuantityRequiredHigherThanAvailableQuantityException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE;

    public QuantityRequiredHigherThanAvailableQuantityException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
