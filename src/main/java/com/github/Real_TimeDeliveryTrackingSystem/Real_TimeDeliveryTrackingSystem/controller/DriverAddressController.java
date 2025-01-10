package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract.DriverAddressControllerContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.driver.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/driverAddress")
public class DriverAddressController implements DriverAddressControllerContract {

    private final DriverService service;

    @Autowired
    public DriverAddressController(DriverService service) {
        this.service = service;
    }


    @Override
    public ResponseEntity<AddressVO> create(AddressVO addressVO) {

        AddressVO createdAddress = service.addAddressToDriver(addressVO);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AddressVO> update(AddressVO addressVO) {

        AddressVO updateAddress = service.updateAddressOfADriver(addressVO);
        return ResponseEntity.ok(updateAddress);
    }

    @Override
    public ResponseEntity<AddressVO> findById(String addressId) {

        AddressVO addressOfACustomerByItsId = service.findAddressOfADriverByItsId(addressId);
        return ResponseEntity.ok(addressOfACustomerByItsId);
    }

    @Override
    public ResponseEntity<Page<AddressVO>> findAll(Pageable pageable) {

        Page<AddressVO> allAddressesOfACustomer = service.findAllAddressesOfADriver(pageable);
        return ResponseEntity.ok(allAddressesOfACustomer);
    }

    @Override
    public ResponseEntity<Void> delete(String addressId) {

        service.deleteAddressOfADriver(addressId);

        return ResponseEntity.noContent().build();
    }
}
