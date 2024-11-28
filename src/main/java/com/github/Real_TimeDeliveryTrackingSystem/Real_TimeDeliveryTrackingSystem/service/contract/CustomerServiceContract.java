package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.LicensePlateNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.VehicleNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service contract interface for vehicle operations in the Real-Time Delivery Tracking System.
 * This interface provides methods for creating, updating, retrieving, and deleting vehicles,
 * as well as retrieving a paginated list of all vehicles.
 */
public interface CustomerServiceContract {
    /**
     * Creates a new vehicle.
     *
     * @param vehicleVO the vehicle data to be created
     * @return the created {@link VehicleVO} object
     * @throws InvalidLicensePlateException
     * @throws DuplicatedLicensePlateException
     * @throws VehicleNotFoundException
     * @throws LicensePlateNotFoundException
     * @see VehicleVO
     * @see VehicleEntity
     */
    CustomerVO create(CustomerVO customerVO);

    /**
     * Updates an existing vehicle.
     *
     * @param vehicleVO the updated vehicle data
     * @return the updated {@link VehicleVO} object
     * @throws InvalidLicensePlateException
     * @throws DuplicatedLicensePlateException
     * @throws VehicleNotFoundException
     * @throws LicensePlateNotFoundException
     * @see VehicleVO
     * @see VehicleEntity
     */
    CustomerVO update(CustomerVO customerVO);

    /**
     * Finds a vehicle by its unique identifier.
     *
     * @param id the ID of the vehicle to be found
     * @return the found {@link VehicleVO} object
     * @throws VehicleNotFoundException
     * @see VehicleVO
     * @see VehicleEntity
     */
    CustomerVO findById(CustomerVO id);


    /**
     * Retrieves a paginated list of all vehicles.
     *
     * @param pageable the pagination information
     * @return a {@link Page} of {@link VehicleVO} objects
     * @see VehicleVO
     * @see VehicleEntity
     */

    Page<CustomerVO> findAll(final Pageable pageable);

    /**
     * Deletes a vehicle by its unique identifier.
     *
     * @param id the ID of the vehicle to be deleted
     * @throws VehicleNotFoundException
     * @see VehicleVO
     * @see VehicleEntity
     */
    void delete(String id);
}

