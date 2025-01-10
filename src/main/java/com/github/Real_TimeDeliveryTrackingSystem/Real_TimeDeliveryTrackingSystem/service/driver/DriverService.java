package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.driver;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DriverNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.VehicleNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.vehicle.VehicleService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.DriverServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DriverService implements DriverServiceContract {

    private static final String DRIVER_NOT_FOUND_MESSAGE = "This driver was not found, please verify the fields and try again.";
    private static final String INVALID_PASSWORD_MESSAGE = "The password typed is incorrect," +
            " please verify and try again.";

    private static final String VEHICLE_NOT_ASSOCIATED_MESSAGE = "That vehicle was not associated with this user," +
            " please verify the fields and try again.";

    private final DriverRepository repository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final VehicleService vehicleService;

    @Autowired
    public DriverService(DriverRepository repository, AddressService addressService, PasswordEncoder passwordEncoder, VehicleService vehicleService) {
        this.repository = repository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.vehicleService = vehicleService;
    }

    @Override
    @Transactional
    public void delete(PasswordDTO passwordDTO) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));


        if (!passwordEncoder.matches(passwordDTO.getPassword(), driverEntity.getUser().getPassword())) {
            throw new InvalidPasswordException(INVALID_PASSWORD_MESSAGE);

        }


        repository.delete(driverEntity);

    }


    @Override
    public AddressVO addAddressToDriver(AddressVO addressVO) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        AddressVO createdAddress = addressService.create(addressVO);
        driverEntity.getAddresses().add(BuildMapper.parseObject(new AddressEntity(), createdAddress));
        repository.save(driverEntity);

        return createdAddress;
    }

    @Override
    public AddressVO updateAddressOfADriver(AddressVO addressVO) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        addressService.verifyIfAddressIdIsAssociatedWithUser(addressVO.getId(), driverEntity.getAddresses());


        return addressService.update(addressVO);
    }

    @Override
    public AddressVO findAddressOfADriverByItsId(String addressId) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        addressService.verifyIfAddressIdIsAssociatedWithUser(addressId, driverEntity.getAddresses());

        return addressService.findById(addressId);
    }

    @Override
    public Page<AddressVO> findAllAddressesOfADriver(Pageable pageable) {

        Page<AddressEntity> addressEntities = repository.findAddressesByDriverEmail(retrieveUserEmail(), pageable);


        return addressEntities.map(addressEntity -> BuildMapper.parseObject(new AddressVO(), addressEntity));
    }

    @Override
    @Transactional
    public void deleteAddressOfADriver(String addressId) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        addressService.verifyIfAddressIdIsAssociatedWithUser(addressId, driverEntity.getAddresses());

        addressService.delete(addressId);

    }

    @Override
    public VehicleVO createVehicle(VehicleVO vehicleVO) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));
        VehicleVO createdVehicle = vehicleService.create(vehicleVO);
        driverEntity.getVehicles().add(BuildMapper.parseObject(new VehicleEntity(), createdVehicle));
        repository.save(driverEntity);

        return createdVehicle;
    }

    @Override
    public VehicleVO updateVehicle(VehicleVO vehicleVO) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        verifyIfVehicleIdIsAssociatedWithDriver(vehicleVO.getId(), driverEntity.getVehicles());

        return vehicleService.update(vehicleVO);
    }

    @Override
    public VehicleVO findVehicleById(String id) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        verifyIfVehicleIdIsAssociatedWithDriver(id, driverEntity.getVehicles());

        return vehicleService.findById(id);
    }

    @Override
    public VehicleVO findVehicleByLicensePlate(String licensePlate) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        verifyIfLicensePlateIdIsAssociatedWithDriver(licensePlate, driverEntity.getVehicles());

        return vehicleService.findByLicensePlate(licensePlate);
    }

    @Override
    public Page<VehicleVO> findAllVehicles(Pageable pageable) {

        Page<VehicleEntity> vehiclesEntities = repository.findVehiclesByDriverEmail(retrieveUserEmail(), pageable);


        return vehiclesEntities.map(vehicleEntity -> BuildMapper.parseObject(new VehicleVO(), vehicleEntity));
    }

    @Override
    @Transactional
    public void deleteVehicle(String id) {

        DriverEntity driverEntity = repository.findDriverByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new DriverNotFoundException(DRIVER_NOT_FOUND_MESSAGE));

        verifyIfVehicleIdIsAssociatedWithDriver(id, driverEntity.getVehicles());

        vehicleService.delete(id);

    }

    private void verifyIfVehicleIdIsAssociatedWithDriver(String vehicleId, List<VehicleEntity> vehicles) {

        for (VehicleEntity vehicleEntity : vehicles) {

            if (vehicleId.equals(vehicleEntity.getId())) {
                return;
            }
        }

        throw new VehicleNotFoundException(VEHICLE_NOT_ASSOCIATED_MESSAGE);
    }

    private void verifyIfLicensePlateIdIsAssociatedWithDriver(String licensePlate, List<VehicleEntity> vehicles) {

        for (VehicleEntity vehicleEntity : vehicles) {

            if (licensePlate.equals(vehicleEntity.getLicensePlate())) {
                return;
            }
        }

        throw new VehicleNotFoundException(VEHICLE_NOT_ASSOCIATED_MESSAGE);
    }


    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }


}
