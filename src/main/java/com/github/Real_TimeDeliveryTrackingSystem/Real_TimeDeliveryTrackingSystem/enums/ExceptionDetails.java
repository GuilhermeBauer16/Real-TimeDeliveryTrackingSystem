package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums;

import org.springframework.http.HttpStatus;


public enum ExceptionDetails {

    //Address
    ADDRESS_NOT_FOUND_MESSAGE("Occur an error in find an address for the reason: %s", HttpStatus.NOT_FOUND),
    INVALID_ADDRESS_MESSAGE("Occur an error into the registration of an address in reason of: %s", HttpStatus.BAD_REQUEST),

    //Customer
    CUSTOMER_NOT_FOUND_MESSAGE("Occur an error in find the Customer for the reason: %s", HttpStatus.NOT_FOUND),
    INVALID_CUSTOMER_MESSAGE("Occur an error into the registration of a customer in reason of: %s", HttpStatus.BAD_REQUEST),

    //Vehicle
    VEHICLE_NOT_FOUND_MESSAGE("Occur an error in find the vehicle for the reason: %s", HttpStatus.NOT_FOUND),
    LICENSE_PLATE_NOT_FOUND_MESSAGE("Occur an error in find the license plate for the reason: %s", HttpStatus.NOT_FOUND),
    INVALID_LICENSE_PLATE_MESSAGE("These license plate is invalid for the reason: %s", HttpStatus.BAD_REQUEST),
    DUPLICATED_LICENSE_PLATE_MESSAGE("These license plate is invalid for the reason: %s", HttpStatus.BAD_REQUEST),

    //User
    USER_NOT_FOUND_MESSAGE("Occur an error in user for the reason: %s", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND_MESSAGE("Occur an error in find the role for the reason: %s", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_REGISTER_MESSAGE("Occur an error into the registration of the email in reason of: %s", HttpStatus.BAD_REQUEST),
    ROLE_ALREADY_REGISTER_MESSAGE("Occur an error into the registration of the role in reason of: %s", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN_MESSAGE("Occur an error to generate the token for the reason: %s", HttpStatus.BAD_REQUEST),
    INVALID_USER_CREDENTIALS_MESSAGE("Occur an error to registry the user for the reason: %s", HttpStatus.FORBIDDEN),
    INVALID_PASSWORD_MESSAGE("These password is invalid for the reason: %s", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_PATTERN_MESSAGE("Occur an error into the registration of the email in reason of: %s", HttpStatus.BAD_REQUEST),
    USER_ALREADY_AUTHENTICATED_MESSAGE("Occur an error into the authenticate the user in reason of: %s", HttpStatus.BAD_REQUEST),
    USER_NOT_AUTHENTICATED_MESSAGE("Occur an error into the authenticate the user in reason of: %s", HttpStatus.UNAUTHORIZED),
    INVALID_VERIFICATION_CODE_MESSAGE("Occur an error into evaluated the verification code in reason of: %s", HttpStatus.BAD_REQUEST),
    EXPIRED_VERIFICATION_CODE_MESSAGE("Occur an error into evaluated the verification code in reason of: %s", HttpStatus.BAD_REQUEST),

    //Driver
    INVALID_DRIVER_MESSAGE("Occur an error into the registration of a driver in reason of: %s", HttpStatus.BAD_REQUEST),
    INVALID_DRIVER_LICENSE_MESSAGE("Occur an error into the registration of a driver license in reason of: %s", HttpStatus.BAD_REQUEST),
    DRIVER_LICENSE_ALREADY_REGISTER_MESSAGE("Occur an error into the registration of the driver license in reason of: %s", HttpStatus.BAD_REQUEST),
    DRIVER_NOT_FOUND_MESSAGE("Occur an error in find the Driver for the reason: %s", HttpStatus.NOT_FOUND),

    //Utils
    INVALID_UUID_FORMAT_MESSAGE("The ID %s needs to be in a UUID format", HttpStatus.BAD_REQUEST),
    QUANTITY_LOWER_THAN_ONE_MESSAGE("Occur an error into the registration of a quantity in reason of: %s", HttpStatus.BAD_REQUEST),
    PRICE_LOWER_THAN_ZERO_MESSAGE("Occur an error into the registration of a price in reason of: %s", HttpStatus.BAD_REQUEST),
    QUANTITY_REQUIRED_HIGHER_THAN_AVAILABLE_QUANTITY_MESSAGE("Occur an error into the registration of a quantity in reason of: %s", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_NUMBER_MESSAGE("Occur an error into the registration of a phone number in reason of: %s", HttpStatus.BAD_REQUEST),
    FIELD_NOT_FOUND_MESSAGE("The field %s can not be null or empty!", HttpStatus.NOT_FOUND),
    EXCEPTION_TYPE_NOT_THROWN("Can not thrown the exception of the type: %s", HttpStatus.NOT_FOUND),

    // Product

    INVALID_PRODUCT_MESSAGE("Occur an error into find the product for the reason: %s", HttpStatus.NOT_FOUND),
    EMPTY_PRODUCT_LIST_MESSAGE("Occur an error in find the product list for the reason: %s", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_FOUND_MESSAGE("Have an error in find the product for the reason: %s", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_ASSOCIATED_WITH_CUSTOMER_MESSAGE("Occur an error in find the product for the reason: %s", HttpStatus.BAD_REQUEST),

    // Shopping Cart
    SHOPPING_CART_NOT_FOUND_MESSAGE("Occur an error in find the product for the reason: %s", HttpStatus.NOT_FOUND),

    // Mercado Pago
    MERCADO_PAGO_EXCEPTION_MESSAGE("Occur an error into connected with mercado pago for the reason: %s", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;

    ExceptionDetails(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String formatErrorMessage(String message) {
        return String.format(this.message, message);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
