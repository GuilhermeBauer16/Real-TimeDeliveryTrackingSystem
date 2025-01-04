package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.DriverVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DriverLicenseAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidDriverException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.DriverFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.DriverRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.DriverRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.UserRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.DriverRegistrationServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.DriverLicenseValidatorUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PhoneNumberValidator;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import com.google.i18n.phonenumbers.NumberParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DriverRegistrationService implements DriverRegistrationServiceContract {


    private static final String INVALID_DRIVER_MESSAGE = "This Driver is invalid, please verify the fields and try again.";
    private static final String DRIVER_LICENSE_ALREADY_REGISTER_MESSAGE = "This driver license is already registered, " +
            "please verify the fields and try again.";

    private static final String PHONE_PREFIX = "+";


    private final UserRegistrationService userRegistrationService;

    private final DriverRepository repository;

    private final AddressService addressService;

    private final VehicleService vehicleService;

    private final DriverRepository driverRepository;

    @Autowired
    public DriverRegistrationService(UserRegistrationService userRegistrationService, DriverRepository repository, AddressService addressService, VehicleService vehicleService, DriverRepository driverRepository) {
        this.userRegistrationService = userRegistrationService;
        this.repository = repository;
        this.addressService = addressService;
        this.vehicleService = vehicleService;
        this.driverRepository = driverRepository;
    }


    @Override
    public DriverRegistrationResponse create(DriverVO driverVO) throws NumberParseException {

        ValidatorUtils.checkObjectIsNullOrThrowException(driverVO, INVALID_DRIVER_MESSAGE, InvalidDriverException.class);
        driverVO.getUser().setUserProfile(UserProfile.ROLE_DRIVER);

        driverVO.setPhoneNumber(PHONE_PREFIX + driverVO.getPhoneNumber());

        PhoneNumberValidator.validatePhoneNumber(driverVO.getPhoneNumber());

        DriverLicenseValidatorUtils.validateDriverLicense(driverVO.getDriverLicense());
        validIfDriverLicenseAlreadyRegistered(driverVO.getDriverLicense());

        List<AddressEntity> savedAddresses = addressService.createAddresses(driverVO.getAddresses());
        List<VehicleEntity> savedVehicles = saveVehicles(driverVO.getVehicles());
        UserVO userVO = BuildMapper.parseObject(new UserVO(), driverVO.getUser());

        UserVO user = userRegistrationService.createUser(userVO);
        UserEntity userEntity = BuildMapper.parseObject(new UserEntity(), user);

        DriverEntity driverEntity = DriverFactory.create(driverVO.getPhoneNumber(), driverVO.getDriverLicense(), savedAddresses, userEntity, savedVehicles);
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(driverEntity, INVALID_DRIVER_MESSAGE, FieldNotFound.class);
        DriverEntity savedDriver = repository.save(driverEntity);
        DriverRegistrationResponse driverRegistrationResponse = BuildMapper.parseObject(new DriverRegistrationResponse(), savedDriver);

        driverRegistrationResponse.setUserRegistrationResponse(
                BuildMapper.parseObject(new UserRegistrationResponse(), savedDriver.getUser()));


        return driverRegistrationResponse;

    }

    private List<VehicleEntity> saveVehicles(List<VehicleEntity> vehicleEntities) {
        List<VehicleEntity> savedVehicles = new ArrayList<>();
        for (VehicleEntity vehicle : vehicleEntities) {

            VehicleVO vehicleVO = BuildMapper.parseObject(new VehicleVO(), vehicle);
            savedVehicles.add(BuildMapper.parseObject(new VehicleEntity(), vehicleService.create(vehicleVO)));
        }

        return savedVehicles;
    }

    private void validIfDriverLicenseAlreadyRegistered(String driverLicense) {

        if (driverRepository.findDriverByDriverLicense(driverLicense).isPresent()){
            throw  new DriverLicenseAllReadyRegisterException(DRIVER_LICENSE_ALREADY_REGISTER_MESSAGE);
        }
    }


}
