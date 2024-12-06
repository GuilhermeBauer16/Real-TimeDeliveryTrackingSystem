package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


public interface CustomerControllerContract {


    @DeleteMapping(value = "/{email}")
    ResponseEntity<Void> delete(@PathVariable("email")String email);

    @PostMapping(value = "/addAddress")
    ResponseEntity<AddressVO> addAddressToCustomer(@RequestBody AddressVO addressVO);

    @PutMapping("/updateAddress")
    ResponseEntity<AddressVO> updateAddressOfACustomer(@RequestBody AddressVO addressVO);

    @GetMapping(value = "/findAddress/{id}")
    ResponseEntity<AddressVO> findAddressOfACustomerByItsId(@PathVariable("id")String AddressId);

    @GetMapping(value = "/findAllAddresses")
    ResponseEntity<Page<AddressVO>> findAllAddressesOfACustomer(Pageable pageable);

    @DeleteMapping(value = "/deleteAddress/{id}")
    ResponseEntity<Void> deleteAddressOfACustomer(@PathVariable("id") String addressId);

    
}
