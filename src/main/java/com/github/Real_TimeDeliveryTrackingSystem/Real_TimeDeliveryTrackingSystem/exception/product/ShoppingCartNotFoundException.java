package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShoppingCartNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.SHOPPING_CART_NOT_FOUND_MESSAGE;

    public ShoppingCartNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
