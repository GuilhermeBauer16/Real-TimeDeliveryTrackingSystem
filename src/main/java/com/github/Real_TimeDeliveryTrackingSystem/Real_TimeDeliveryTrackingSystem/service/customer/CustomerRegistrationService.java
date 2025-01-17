package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.customer.InvalidCustomerException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.CustomerFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.CustomerRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.UserRegistrationResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserRegistrationService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.CustomerRegistrationServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PhoneNumberValidator;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import com.google.i18n.phonenumbers.NumberParseException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRegistrationService implements CustomerRegistrationServiceContract {

    private static final String INVALID_CUSTOMER_MESSAGE = "This customer is invalid, please verify the fields and try again.";

    private static final String PHONE_PREFIX = "+";


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
    public CustomerRegistrationResponse create(CustomerVO customerVO) throws NumberParseException, MessagingException {

        ValidatorUtils.checkObjectIsNullOrThrowException(customerVO, INVALID_CUSTOMER_MESSAGE, InvalidCustomerException.class);
        customerVO.getUser().setUserProfile(UserProfile.ROLE_CUSTOMER);

        customerVO.setPhoneNumber(PHONE_PREFIX + customerVO.getPhoneNumber());
        PhoneNumberValidator.validatePhoneNumber(customerVO.getPhoneNumber());

        List<AddressEntity> addressEntities = addressService.createAddresses(customerVO.getAddresses());
        UserVO userVO = BuildMapper.parseObject(new UserVO(), customerVO.getUser());
        UserVO user = userRegistrationService.createUser(userVO);
        UserEntity userEntity = BuildMapper.parseObject(new UserEntity(), user);
        CustomerEntity customerEntity = CustomerFactory.create(customerVO.getPhoneNumber(), addressEntities, userEntity);
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(customerEntity, INVALID_CUSTOMER_MESSAGE, FieldNotFound.class);
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);
        CustomerRegistrationResponse customerRegistrationResponse = BuildMapper.parseObject(new CustomerRegistrationResponse(), savedCustomer);

        customerRegistrationResponse.setUserRegistrationResponse(
                BuildMapper.parseObject(new UserRegistrationResponse(), savedCustomer.getUser()));


        return customerRegistrationResponse;
    }


}
