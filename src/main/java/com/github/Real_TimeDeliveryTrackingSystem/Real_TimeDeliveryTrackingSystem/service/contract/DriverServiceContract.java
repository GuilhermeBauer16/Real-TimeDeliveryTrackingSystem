package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.LicensePlateNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.VehicleNotFoundException;
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

    void delete(PasswordDTO passwordDTO);

    AddressVO addAddressToDriver(AddressVO addressVO);


    AddressVO updateAddressOfADriver(AddressVO addressVO);

    AddressVO findAddressOfADriverByItsId(String addressId);

    Page<AddressVO> findAllAddressesOfADriver(Pageable pageable);

    void deleteAddressOfADriver(String addressId);

    VehicleVO createVehicle(VehicleVO vehicleVO);

    VehicleVO updateVehicle(VehicleVO vehicleVO);

    VehicleVO findVehicleById(String id);

    VehicleVO findVehicleByLicensePlate(String licensePlate);

    Page<VehicleVO> findAllVehicles(final Pageable pageable);


    void deleteVehicle(String id);

}

