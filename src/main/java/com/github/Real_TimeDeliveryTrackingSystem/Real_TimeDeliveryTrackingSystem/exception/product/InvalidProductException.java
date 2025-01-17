package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProductException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.INVALID_PRODUCT_MESSAGE;

    public InvalidProductException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
