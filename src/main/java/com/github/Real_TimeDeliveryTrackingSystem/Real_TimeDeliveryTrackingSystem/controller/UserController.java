package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signIn")
public class UserController {

    private final UserRegistrationService service;

    @Autowired
    public UserController(UserRegistrationService userRegistrationService) {
        this.service = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity<UserVO> create(@RequestBody UserVO userVO)  {
        UserVO createdVehicle = service.createUser(userVO);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }


}
