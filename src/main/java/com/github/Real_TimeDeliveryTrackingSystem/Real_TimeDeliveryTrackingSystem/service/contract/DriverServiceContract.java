package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contract for the Driver Service, defining operations related to drivers, their addresses, and vehicles.
 * This interface provides methods for managing driver information, addresses, and vehicles.
 */
public interface DriverServiceContract {

    /**
     * Deletes a driver after validating their password.
     *
     * @param passwordDTO the password data transfer object containing the driver's credentials for validation
     * @see PasswordDTO
     */
    void delete(PasswordDTO passwordDTO);

    /**
     * Adds a new address for a driver.
     *
     * @param addressVO the address value object containing the details of the address to be added
     * @return the created {@link AddressVO}
     * @see AddressVO
     * @see AddressService
     */
    AddressVO addAddressToDriver(AddressVO addressVO);

    /**
     * Updates an existing address of a driver.
     *
     * @param addressVO the address value object containing the updated address details
     * @return the updated {@link AddressVO}
     * @see AddressVO
     * @see AddressService
     */
    AddressVO updateAddressOfADriver(AddressVO addressVO);

    /**
     * Finds an address of a driver by its unique identifier.
     *
     * @param addressId the unique identifier of the address
     * @return the {@link AddressVO} associated with the given identifier
     * @see AddressVO
     * @see AddressService
     */
    AddressVO findAddressOfADriverByItsId(String addressId);

    /**
     * Retrieves a paginated list of all addresses associated with a driver.
     *
     * @param pageable the pagination information
     * @return a {@link Page} containing {@link AddressVO} objects
     * @see AddressVO
     * @see AddressService
     * @see Pageable
     */
    Page<AddressVO> findAllAddressesOfADriver(Pageable pageable);

    /**
     * Deletes an address associated with a driver by its unique identifier.
     *
     * @param addressId the unique identifier of the address to be deleted
     * @see AddressService
     */
    void deleteAddressOfADriver(String addressId);

    /**
     * Creates a new vehicle for a driver.
     *
     * @param vehicleVO the vehicle value object containing the details of the new vehicle
     * @return the created {@link VehicleVO}
     * @see VehicleVO
     * @see VehicleService
     */
    VehicleVO createVehicle(VehicleVO vehicleVO);

    /**
     * Updates the details of an existing vehicle.
     *
     * @param vehicleVO the vehicle value object containing the updated vehicle details
     * @return the updated {@link VehicleVO}
     * @see VehicleVO
     * @see VehicleService
     */
    VehicleVO updateVehicle(VehicleVO vehicleVO);

    /**
     * Finds a vehicle by its unique identifier.
     *
     * @param id the unique identifier of the vehicle
     * @return the {@link VehicleVO} associated with the given identifier
     * @see VehicleService
     */
    VehicleVO findVehicleById(String id);

    /**
     * Finds a vehicle by its license plate number.
     *
     * @param licensePlate the license plate number of the vehicle
     * @return the {@link VehicleVO} associated with the given license plate
     * @see VehicleService
     */
    VehicleVO findVehicleByLicensePlate(String licensePlate);

    /**
     * Retrieves a paginated list of all vehicles associated with a driver.
     *
     * @param pageable the pagination information
     * @return a {@link Page} containing {@link VehicleVO} objects
     * @see VehicleVO
     * @see VehicleService
     * @see Pageable
     */
    Page<VehicleVO> findAllVehicles(Pageable pageable);

    /**
     * Deletes a vehicle by its unique identifier.
     *
     * @param id the unique identifier of the vehicle to be deleted
     * @see VehicleService
     */
    void deleteVehicle(String id);
}
