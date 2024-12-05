package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.CustomerControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController implements CustomerControllerContract {

    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<CustomerVO> update(CustomerVO customerVO) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(String id) {
        return null;
    }

    @Override
    public ResponseEntity<AddressVO> addAddressToCustomer(AddressVO addressVO) {
        return null;
    }

    @Override
    public ResponseEntity<AddressVO> updateAddressOfACustomer(AddressVO addressVO) {
        return null;
    }

    @Override
    public ResponseEntity<AddressVO> findAddressOfACustomerByItsId(String AddressId) {
        return null;
    }

    @Override
    public ResponseEntity<Page<AddressVO>> findAllAddressesOfACustomer(Pageable pageable) {
        Page<AddressVO> allAddressesOfACustomer = service.findAllAddressesOfACustomer(pageable);
        return ResponseEntity.ok(allAddressesOfACustomer);
    }

    @Override
    public ResponseEntity<Void> deleteAddressOfACustomer(String AddressId) {
        return null;
    }
}
