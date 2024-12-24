package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service contract for managing customers and their addresses.
 *
 * <p>This interface defines the operations for managing customer-related data,
 * including deleting a customer, managing customer addresses, and retrieving address information.</p>
 *
 * @see AddressVO
 * @see Page
 * @see Pageable
 */
public interface DriverServiceContract {

    /**
     * Deletes a customer by their password.
     *
     * <p>This method removes a customer record from the system based on the provided password address.</p>
     *
     * @param passwordDTO the password address of the customer to delete
     */
    void delete(PasswordDTO passwordDTO);

    /**
     * Adds a new address to a customer.
     *
     * <p>This method associates a new {@link AddressVO} object with a customer.</p>
     *
     * @param addressVO the address details encapsulated in an {@link AddressVO} object
     * @return the added {@link AddressVO} object
     */
    AddressVO addAddressToDriver(AddressVO addressVO);

    /**
     * Updates an existing address of a customer.
     *
     * <p>This method updates the details of an existing address for a customer using the provided {@link AddressVO} object.</p>
     *
     * @param addressVO the updated address details encapsulated in an {@link AddressVO} object
     * @return the updated {@link AddressVO} object
     */
    AddressVO updateAddressOfADriver(AddressVO addressVO);

    /**
     * Finds a specific address of a customer by its unique identifier.
     *
     * <p>This method retrieves an {@link AddressVO} object associated with a customer based on the provided address ID.</p>
     *
     * @param addressId the unique identifier of the address
     * @return the {@link AddressVO} object representing the address, or {@code null} if not found
     */
    AddressVO findAddressOfADriverByItsId(String addressId);

    /**
     * Retrieves a paginated list of all addresses associated with a customer.
     *
     * <p>This method returns a {@link Page} of {@link AddressVO} objects representing all the addresses
     * associated with a customer, based on the pagination and sorting information provided in the {@link Pageable} object.</p>
     *
     * @param pageable the pagination and sorting information encapsulated in a {@link Pageable} object
     * @return a {@link Page} of {@link AddressVO} objects
     */
    Page<AddressVO> findAllAddressesOfADriver(Pageable pageable);

    /**
     * Deletes a specific address of a customer by its unique identifier.
     *
     * <p>This method removes an address record associated with a customer from the system based on the provided address ID.</p>
     *
     * @param addressId the unique identifier of the address to delete
     */
    void deleteAddressOfADriver(String addressId);

}

