package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MercadoPagoException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.MERCADO_PAGO_EXCEPTION_MESSAGE;

    public MercadoPagoException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
