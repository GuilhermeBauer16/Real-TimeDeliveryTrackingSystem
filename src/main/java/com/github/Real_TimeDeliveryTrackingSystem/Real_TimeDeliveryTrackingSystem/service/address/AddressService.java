package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.AddressNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.address.InvalidAddressException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.AddressFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.AddressServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService implements AddressServiceContract {

    private static final String INVALID_ADDRESS_MESSAGE = "This address is invalid, please verify the fields and try again.";
    private static final String ADDRESS_NOT_FOUND_MESSAGE = "This address can't be find, please verify the fields and try again.";
    private static final String ADDRESS_NOT_ASSOCIATED_MESSAGE = "That address was not associated with this user," +
            " please verify the fields and try again.";

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


    @Override
    public AddressVO create(AddressVO address) {

        ValidatorUtils.checkObjectIsNullOrThrowException(address, INVALID_ADDRESS_MESSAGE, InvalidAddressException.class);
        AddressEntity addressFactory = AddressFactory.create(address.getStreet(), address.getCity(), address.getState()
                , address.getPostalCode(), address.getCountry());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(addressFactory, INVALID_ADDRESS_MESSAGE, FieldNotFound.class);
        AddressEntity addressEntity = addressRepository.save(addressFactory);
        return BuildMapper.parseObject(new AddressVO(), addressEntity);

    }

    @Override
    public List<AddressEntity> createAddresses(List<AddressEntity> addresses) {

        List<AddressEntity> savedAddresses = new ArrayList<>();
        for (AddressEntity address : addresses) {

            AddressVO addressVO = BuildMapper.parseObject(new AddressVO(), address);
            savedAddresses.add(BuildMapper.parseObject(new AddressEntity(), create(addressVO)));
        }

        return savedAddresses;
    }

    @Override
    public AddressVO update(AddressVO addressVO) {

        AddressEntity addressEntity = addressRepository.findById(addressVO.getId())
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MESSAGE));

        ValidatorUtils.checkObjectIsNullOrThrowException(addressVO, INVALID_ADDRESS_MESSAGE, InvalidAddressException.class);
        AddressEntity updatedAddressFields = ValidatorUtils.updateFieldIfNotNull(addressEntity, addressVO, INVALID_ADDRESS_MESSAGE, FieldNotFound.class);
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(updatedAddressFields, INVALID_ADDRESS_MESSAGE, FieldNotFound.class);
        AddressEntity updatedAddressEntity = addressRepository.save(updatedAddressFields);

        return BuildMapper.parseObject(new AddressVO(), updatedAddressEntity);
    }

    @Override
    public AddressVO findById(String id) {

        AddressEntity addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MESSAGE));

        return BuildMapper.parseObject(new AddressVO(), addressEntity);
    }


    @Override
    public void delete(String id) {

        AddressEntity addressEntity = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(ADDRESS_NOT_FOUND_MESSAGE));

        addressRepository.delete(addressEntity);

    }


    @Override
    public void verifyIfAddressIdIsAssociatedWithUser(String addressId, List<AddressEntity> addressEntities) {

        for (AddressEntity addressEntity : addressEntities) {

            if (addressId.equals(addressEntity.getId())) {
                return;
            }
        }

        throw new AddressNotFoundException(ADDRESS_NOT_ASSOCIATED_MESSAGE);

    }

    @Override
    public void deleteAllAddresses(List<AddressVO> addressVOS) {

        for (AddressVO addressVO : addressVOS) {

            delete(addressVO.getId());
        }

    }

}
