package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service contract for managing addresses.
 *
 * <p>This interface defines the operations for managing address entities,
 * including creating, updating, finding, listing, and deleting addresses.</p>
 *
 * @see AddressVO
 * @see Page
 * @see Pageable
 */
public interface AddressServiceContract {

    /**
     * Creates a new address.
     *
     * <p>This method takes an {@link AddressVO} object containing the address details
     * and creates a new address record in the system.</p>
     *
     * @param addressVO the address details encapsulated in an {@link AddressVO} object
     * @return the created {@link AddressVO} object
     */
    AddressVO create(AddressVO addressVO);

    /**
     * Creates multiple addresses.
     *
     * <p>This method takes a list of {@link AddressEntity} objects and creates multiple address records in the system.</p>
     *
     * @param addresses a list of {@link AddressEntity} objects representing the addresses to create
     * @return the list of created {@link AddressEntity} objects
     */

    List<AddressEntity> createAddresses(List<AddressEntity> addresses);


    /**
     * Updates an existing address.
     *
     * <p>This method updates the details of an existing address using the provided
     * {@link AddressVO} object.</p>
     *
     * @param addressVO the updated address details encapsulated in an {@link AddressVO} object
     * @return the updated {@link AddressVO} object
     */
    AddressVO update(AddressVO addressVO);

    /**
     * Finds an address by its unique identifier.
     *
     * <p>This method retrieves an address record based on the provided unique ID.</p>
     *
     * @param id the unique identifier of the address
     * @return the {@link AddressVO} object representing the address, or {@code null} if not found
     */
    AddressVO findById(String id);

    /**
     * Deletes an address by its unique identifier.
     *
     * <p>This method deletes an address record from the system based on the provided unique ID.</p>
     *
     * @param id the unique identifier of the address to delete
     */
    void delete(String id);

    /**
     * Verifies if the given address ID is associated with any user.
     *
     * <p>This method checks whether the specified address ID exists in the provided list of
     * {@link AddressEntity} objects.</p>
     *
     * @param addressId       the unique identifier of the address
     * @param addressEntities a list of {@link AddressEntity} objects to verify against
     */
    void verifyIfAddressIdIsAssociatedWithUser(String addressId, List<AddressEntity> addressEntities);

    /**
     * Deletes multiple addresses.
     *
     * <p>This method deletes all the addresses provided in the list of {@link AddressVO} objects.</p>
     *
     * @param addressVOS a list of {@link AddressVO} objects representing the addresses to delete
     */
    void deleteAllAddresses(List<AddressVO> addressVOS);
}
