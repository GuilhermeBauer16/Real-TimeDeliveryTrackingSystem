package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.PRODUCT_NOT_FOUND_MESSAGE;

    public ProductNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
