package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.handler;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.AddressNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.customer.CustomerNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DriverLicenseAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DriverNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.EmailAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.ExceptionResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.ExpiredVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.customer.InvalidCustomerException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.InvalidDriverException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.InvalidDriverLicenseException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.InvalidEmailPatternException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.InvalidPhoneNumberException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.InvalidVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.LicensePlateNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserCredentialsNotMatchedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.ValidationUtilsException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.VehicleNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler {

    @ExceptionHandler({
            FieldNotFound.class,
            VehicleNotFoundException.class,
            LicensePlateNotFoundException.class,
            UserNotFoundException.class,
            CustomerNotFoundException.class,
            AddressNotFoundException.class,
            DriverNotFoundException.class,
            ProductNotFoundException.class

    })
    public final ResponseEntity<ExceptionResponse> handlerNotFoundException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);

    }


    @ExceptionHandler({ValidationUtilsException.class,
            InvalidLicensePlateException.class,
            DuplicatedLicensePlateException.class,
            EmailAllReadyRegisterException.class,
            DriverLicenseAllReadyRegisterException.class,
            InvalidEmailPatternException.class,
            InvalidCustomerException.class,
            InvalidPasswordException.class,
            InvalidPhoneNumberException.class,
            InvalidDriverLicenseException.class,
            InvalidDriverException.class,
            UserAlreadyAuthenticatedException.class,
            ExpiredVerificationCodeException.class,
            InvalidVerificationCodeException.class,})
    public final ResponseEntity<ExceptionResponse> handlerBadRequestException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({UserCredentialsNotMatchedException.class,})
    public final ResponseEntity<ExceptionResponse> handlerForbiddenException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);


    }

    @ExceptionHandler({UserNotAuthenticatedException.class,})
    public final ResponseEntity<ExceptionResponse> handlerUnauthorizedException(
            Exception ex,
            WebRequest webRequest
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ex.getMessage(),
                webRequest.getDescription(false),
                new Date()
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);

    }
}
