package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.CustomerAddressControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customerAddress")
public class CustomerAddressController implements CustomerAddressControllerContract {

    private final CustomerService service;

    @Autowired
    public CustomerAddressController(CustomerService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<AddressVO> addAddressToCustomer(AddressVO addressVO) {

        AddressVO createdAddress = service.addAddressToCustomer(addressVO);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AddressVO> updateAddressOfACustomer(AddressVO addressVO) {

        AddressVO updateAddress = service.updateAddressOfACustomer(addressVO);
        return ResponseEntity.ok(updateAddress);
    }

    @Override
    public ResponseEntity<AddressVO> findAddressOfACustomerByItsId(String addressId) {
        AddressVO addressOfACustomerByItsId = service.findAddressOfACustomerByItsId(addressId);
        return ResponseEntity.ok(addressOfACustomerByItsId);
    }

    @Override
    public ResponseEntity<Page<AddressVO>> findAllAddressesOfACustomer(Pageable pageable) {
        Page<AddressVO> allAddressesOfACustomer = service.findAllAddressesOfACustomer(pageable);
        return ResponseEntity.ok(allAddressesOfACustomer);
    }

    @Override
    public ResponseEntity<Void> deleteAddressOfACustomer(String addressId) {

        service.deleteAddressOfACustomer(addressId);

        return ResponseEntity.noContent().build();
    }
}
