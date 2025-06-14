package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.dto.PasswordDTO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.customer.CustomerNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.InvalidPasswordException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.CustomerRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.address.AddressService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.CustomerServiceContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService implements CustomerServiceContract {

    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "This customer was not found, please verify the fields and try again.";

    private static final String INVALID_PASSWORD_MESSAGE = "The password typed is incorrect," +
            " please verify and try again.";

    private final CustomerRepository customerRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressService addressService, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public CustomerVO findCustomerByEmail(String email) {

        CustomerEntity customerEntity = customerRepository.findCustomerByUserEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));


        return BuildMapper.parseObject(new CustomerVO(), customerEntity);
    }

    @Override
    @Transactional
    public void delete(PasswordDTO passwordDTO) {

        CustomerEntity customerEntity = customerRepository.findCustomerByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));

        if (!passwordEncoder.matches(passwordDTO.getPassword(), customerEntity.getUser().getPassword())) {
            throw new InvalidPasswordException(INVALID_PASSWORD_MESSAGE);

        }

        Page<AddressVO> allAddressesOfACustomer = findAllAddressesOfACustomer(Pageable.ofSize(10));
        addressService.deleteAllAddresses(allAddressesOfACustomer.getContent());

        customerRepository.delete(customerEntity);

    }

    @Override
    public AddressVO addAddressToCustomer(AddressVO addressVO) {

        CustomerEntity customerEntity = customerRepository.findCustomerByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));

        AddressVO createdAddress = addressService.create(addressVO);
        customerEntity.getAddresses().add(BuildMapper.parseObject(new AddressEntity(), createdAddress));
        customerRepository.save(customerEntity);

        return createdAddress;
    }

    @Override
    public AddressVO updateAddressOfACustomer(AddressVO addressVO) {

        CustomerEntity customerEntity = customerRepository.findCustomerByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));

        addressService.verifyIfAddressIdIsAssociatedWithUser(addressVO.getId(), customerEntity.getAddresses());


        return addressService.update(addressVO);
    }

    @Override
    public AddressVO findAddressOfACustomerByItsId(String addressId) {

        CustomerEntity customerEntity = customerRepository.findCustomerByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));

        addressService.verifyIfAddressIdIsAssociatedWithUser(addressId, customerEntity.getAddresses());

        return addressService.findById(addressId);
    }

    @Override
    public Page<AddressVO> findAllAddressesOfACustomer(Pageable pageable) {

        Page<AddressEntity> addressEntities = customerRepository.findAddressesByCustomerEmail(retrieveUserEmail(), pageable);


        return addressEntities.map(addressEntity -> BuildMapper.parseObject(new AddressVO(), addressEntity));
    }


    @Override
    @Transactional
    public void deleteAddressOfACustomer(String addressId) {

        CustomerEntity customerEntity = customerRepository.findCustomerByUserEmail(retrieveUserEmail())
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));

        addressService.verifyIfAddressIdIsAssociatedWithUser(addressId, customerEntity.getAddresses());

        addressService.delete(addressId);

    }

    private String retrieveUserEmail() {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getUsername();

    }


}
