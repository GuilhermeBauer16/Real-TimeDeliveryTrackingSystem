package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.InvalidCustomerException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.CustomerFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.CustomerRegistrationServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomerRegistrationService implements CustomerRegistrationServiceContract {

    private static final String INVALID_CUSTOMER_MESSAGE = "This customer is invalid, please verify the fields and try again.";

    private final UserRegistrationService userRegistrationService;

    private final CustomerRepository customerRepository;

    private final AddressService addressService;

    @Autowired
    public CustomerRegistrationService(UserRegistrationService userRegistrationService, CustomerRepository customerRepository, AddressService addressService) {
        this.userRegistrationService = userRegistrationService;
        this.customerRepository = customerRepository;
        this.addressService = addressService;
    }

    @Override
    public CustomerVO create(CustomerVO customerVO) {

        ValidatorUtils.checkObjectIsNullOrThrowException(customerVO,INVALID_CUSTOMER_MESSAGE, InvalidCustomerException.class);
        customerVO.getUser().setUserProfile(UserProfile.ROLE_CUSTOMER);
        Set<AddressEntity> addressEntities = saveAddresses(customerVO.getAddresses());
        UserVO userVO = BuildMapper.parseObject(new UserVO(), customerVO.getUser());
        UserVO user = userRegistrationService.createUser(userVO);
        UserEntity userEntity = BuildMapper.parseObject(new UserEntity(), user);
        CustomerEntity customerEntity = CustomerFactory.create(customerVO.getPhoneNumber(), addressEntities, userEntity);
        CustomerEntity save = customerRepository.save(customerEntity);


        return BuildMapper.parseObject(new CustomerVO(), save);
    }

    private Set<AddressEntity> saveAddresses(Set<AddressEntity> addresses) {
        Set<AddressEntity> savedAddresses = new HashSet<>();
        for (AddressEntity address : addresses) {

            AddressVO addressVO = BuildMapper.parseObject(new AddressVO(), address);
            savedAddresses.add(BuildMapper.parseObject(new AddressEntity(), addressService.create(addressVO)));
        }

        return savedAddresses;
    }


}
