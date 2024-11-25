package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.UserRegistrationResponse;

/**
 * Service interface for managing user registration within the application.
 *
 * <p>This service provides methods to create user based on specific
 * criteria.
 */

public interface UserRegistrationServiceContract {

    /**
     * Registers a new user in the system.
     *
     * <p>This method processes the user registration details provided in the {@link UserVO} object
     * and creates a new user account. It returns a response containing information about the
     * newly created user.
     *
     * @param userVO The {@link UserVO} object containing the user's registration details,
     *               such as username, email, password, and any other relevant information.
     * @return {@link UserRegistrationResponse} object with details of the newly created user,
     *         including a unique user ID and other registration-related information.
     *
     * @throws UserNotFoundException if the {@link UserVO} object was empty or null .
     * @throws FieldNotFound if there is an issue with the registration values, such as empty or null field.
     * @throws EmailAllReadyRegisterException if the email passed is all ready register into the database preventing duplicated values.
     * @see UserRegistrationResponse
     * @see UserVO
     *
     */

    UserVO createUser(UserVO userVO);
}
