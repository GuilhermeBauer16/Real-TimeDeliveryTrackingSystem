package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
import com.google.i18n.phonenumbers.NumberParseException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidDriverException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DriverLicenseAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import jakarta.mail.MessagingException;

/**
 * Contract for the Driver Registration Service, defining the operations related to driver registration.
 * This interface provides a method for creating driver registrations while validating input details.
 */

public interface DriverRegistrationServiceContract {

    /**
     * Registers a new driver using the provided {@link DriverVO}.
     * <p>
     * The method validates the driver's information and processes the registration.
     * If there is an error parsing the phone number, a {@link NumberParseException} is thrown.
     *
     * @param driverVO the driver value object containing the registration details of the driver
     * @return a {@link DriverRegistrationResponse} containing the details of the successful registration
     * @throws NumberParseException if the phone number provided in {@link DriverVO} is invalid or cannot be parsed
     * @throws InvalidDriverException When the provided {@link DriverVO} is invalid or cannot be parsed
     * @throws DriverLicenseAllReadyRegisterException When the driver license provided by the {@link DriverVO} is already register in the system.
     * @see DriverEntity
     * @see DriverVO
     * @see DriverRegistrationResponse
     */

    DriverRegistrationResponse create(DriverVO driverVO) throws NumberParseException, MessagingException;
}
