package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.CustomerNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.CustomerServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements CustomerServiceContract {

    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "This customer was not found, please verify the fields and try again.";
    private static final String INVALID_CUSTOMER_MESSAGE = "This customer is invalid, please verify the fields and try again.";

    private final CustomerRepository customerRepository;
    private final AddressService addressService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressService addressService) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
    }

    @Override
    public CustomerVO update(CustomerVO customerVO) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public AddressVO addAddressToCustomer(AddressVO addressVO) {
        return null;
    }

    @Override
    public AddressVO updateAddressOfACustomer(AddressVO addressVO) {
        return null;
    }

    @Override
    public AddressVO findAddressOfACustomerByItsId(String AddressId) {
        return null;
    }

    @Override
    public Page<AddressVO> findAllAddressesOfACustomer(Pageable pageable) {

        Page<AddressEntity> addressEntities = customerRepository.findAddressesByCustomerEmail(retrieveUserEmail(), pageable);


        return addressEntities.map(addressEntity -> BuildMapper.parseObject(new AddressVO(), addressEntity));
    }


    @Override
    public void deleteAddressOfACustomer(String AddressId) {

    }

    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }
}
