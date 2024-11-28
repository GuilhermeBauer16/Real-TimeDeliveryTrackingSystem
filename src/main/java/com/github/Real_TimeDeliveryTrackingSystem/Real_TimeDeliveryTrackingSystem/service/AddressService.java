package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.AddressFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.AddressRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.AddressServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AddressService implements AddressServiceContract {


    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }


//    private Set<AddressEntity> savedAddress(Set<AddressEntity> addresses) {
//        Set<AddressEntity> savedAddresses = new HashSet<>();
//        for (AddressEntity address : addresses) {
//            AddressEntity addressEntity = AddressFactory.create(address.getStreet(), address.getCity(), address.getState(), address.getPostalCode(), address.getCountry());
//            savedAddresses.add(addressRepository.save(addressEntity));
//        }
//
//        return savedAddresses;
//    }


    @Override
    public AddressEntity create(AddressEntity address) {
        AddressEntity addressEntity = AddressFactory.create(address.getStreet(), address.getCity(), address.getState()
                , address.getPostalCode(), address.getCountry());

        return addressRepository.save(addressEntity);
    }
}
