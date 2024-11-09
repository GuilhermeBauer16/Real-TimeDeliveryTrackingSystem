package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.VehicleNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.VehicleFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.VehicleRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.VehicleServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.LicencePlateUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService implements VehicleServiceContract {
    private static final String DUPLICATED_LICENSE_PLATE_MESSAGE = "That license plate already registered in the system. " +
            "Please verify the license plate and try again!.";
    private static final String VEHICLE_NOT_FOUND = "These Vehicle was Not Found";
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;

    }

    @Override
    public VehicleVO create(VehicleVO vehicleVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(vehicleVO, VEHICLE_NOT_FOUND, VehicleNotFoundException.class);
        String validatedLicencePlate = LicencePlateUtils.validateLicencePlate(vehicleVO.getLicensePlate());
        verifyIfLicensePlateIsNotADuplicateValue(validatedLicencePlate);
        VehicleEntity vehicleEntity = VehicleFactory.create(vehicleVO.getName(), validatedLicencePlate, vehicleVO.getType(),
                vehicleVO.getStatus());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(vehicleEntity, VEHICLE_NOT_FOUND, FieldNotFound.class);
        VehicleEntity savedVehicle = vehicleRepository.save(vehicleEntity);
        return BuildMapper.parseObject(new VehicleVO(), savedVehicle);
    }

    @Override
    public VehicleVO update(VehicleVO vehicleVO) {

        VehicleEntity vehicleEntity = vehicleRepository.findById(vehicleVO.getId())
                .orElseThrow(() -> new VehicleNotFoundException(VEHICLE_NOT_FOUND));
        VehicleEntity updatedVehicleEntity = ValidatorUtils.updateFieldIfNotNull(vehicleEntity
                , vehicleVO, VEHICLE_NOT_FOUND, FieldNotFound.class);
        VehicleEntity savedVehicle = vehicleRepository.save(updatedVehicleEntity);

        return BuildMapper.parseObject(new VehicleVO(), savedVehicle);
    }

    @Override
    public VehicleVO findById(String id) {
        VehicleEntity vehicleEntity = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(VEHICLE_NOT_FOUND));
        return BuildMapper.parseObject(new VehicleVO(), vehicleEntity);
    }

    @Override
    public void delete(String id) {

    }

    private void verifyIfLicensePlateIsNotADuplicateValue(String licensePlate) {

        if (vehicleRepository.findByLicensePlate(licensePlate).isPresent()) {
            throw new DuplicatedLicensePlateException(DUPLICATED_LICENSE_PLATE_MESSAGE);
        }
    }
}
