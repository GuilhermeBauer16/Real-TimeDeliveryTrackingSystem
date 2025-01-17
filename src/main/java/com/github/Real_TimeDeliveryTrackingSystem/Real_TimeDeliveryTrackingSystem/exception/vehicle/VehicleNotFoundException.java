package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.ExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {

    public static final ExceptionDetails ERROR = ExceptionDetails.VEHICLE_NOT_FOUND_MESSAGE;

    public VehicleNotFoundException(String message) {
        super(ERROR.formatErrorMessage(message));
    }
}
