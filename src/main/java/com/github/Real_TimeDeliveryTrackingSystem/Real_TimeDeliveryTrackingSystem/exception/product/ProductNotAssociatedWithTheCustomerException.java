package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product;



import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProductNotAssociatedWithTheCustomerException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.PRODUCT_NOT_ASSOCIATED_WITH_CUSTOMER_MESSAGE;

    public ProductNotAssociatedWithTheCustomerException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
