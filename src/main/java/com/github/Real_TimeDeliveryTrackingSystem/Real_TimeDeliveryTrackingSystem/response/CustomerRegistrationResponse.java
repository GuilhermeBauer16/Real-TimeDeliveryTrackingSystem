package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationResponse {

    private String id;
    private String phoneNumber;
    private List<AddressEntity> addresses;
    private UserRegistrationResponse userRegistrationResponse;
}
