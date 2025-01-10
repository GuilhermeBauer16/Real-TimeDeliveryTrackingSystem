package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.UserUpdateRequest;


/**
 * Service contract interface for user-related operations.
 * <p>
 * This interface defines methods for finding a user by email and updating a user's information. Implementations
 * of this interface should handle business logic related to user management, such as querying user data and
 * performing updates on the user profile.
 * </p>
 */

public interface UserServiceContract {


    /**
     * Finds a user by their email address.
     * <p>
     * This method retrieves a user from the data source based on the provided email. If no user is found with
     * the given email, a {@link UserNotFoundException} is thrown.
     * </p>
     *
     * @param email the email address of the user to be retrieved
     * @return a {@link UserVO} object containing the user's data
     * @throws UserNotFoundException if no user is found with the provided email address
     */
    UserVO findUserByEmail(String email);


    /**
     * Updates a user's information based on the provided update request.
     * <p>
     * This method updates the user's data using the fields from the {@link UserUpdateRequest}. If the user is
     * not found, a {@link UserNotFoundException} is thrown. Fields that are not provided in the update request
     * will not be modified. After updating, the method returns the updated user data as a {@link UserVO} object.
     * </p>
     *
     * @param userUpdateRequest the request containing the user's email and new data to update
     * @return the updated {@link UserVO} object containing the user's data after the update
     * @throws UserNotFoundException if no user is found with the provided email address
     * @throws FieldNotFound         if an invalid field is found in the update request
     */
    UserVO updateUser(UserUpdateRequest userUpdateRequest);
}
