package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
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

    void delete(String email);

    AddressVO addAddressToCustomer(AddressVO addressVO);

    AddressVO updateAddressOfACustomer(AddressVO addressVO);

    AddressVO findAddressOfACustomerByItsId(String addressId);

    Page<AddressVO> findAllAddressesOfACustomer(Pageable pageable);

    void deleteAddressOfACustomer(String addressId);

}

