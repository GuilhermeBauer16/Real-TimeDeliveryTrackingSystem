package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import com.google.i18n.phonenumbers.NumberParseException;

/**
 * Service contract for managing customer registration.
 *
 * <p>This interface defines the contract for customer registration services,
 * including creating new customer records and returning the registration result.</p>
 *
 * @see CustomerVO
 * @see CustomerRegistrationResponse
 */

public interface CustomerRegistrationServiceContract {

    /**
     * Registers a new customer in the system.
     *
     * <p>This method takes a {@link CustomerVO} object containing customer details
     * and creates a new customer record in the system. The result of the registration
     * process is returned as a {@link CustomerRegistrationResponse} object.</p>
     *
     * @param customerVO the customer details encapsulated in a {@link CustomerVO} object
     * @return a {@link CustomerRegistrationResponse} object containing the result of the registration process
     */
    CustomerRegistrationResponse create(CustomerVO customerVO) throws NumberParseException;
}
