package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DriverNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.DriverServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DriverService implements DriverServiceContract {

    private static final String DRIVER_NOT_FOUND_MESSAGE = "This driver was not found, please verify the fields and try again.";
    private static final String INVALID_PASSWORD_MESSAGE = "The password typed is incorrect," +
            " please verify and try again.";

    private final DriverRepository repository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DriverService(DriverRepository repository, AddressService addressService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
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


    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }


}
