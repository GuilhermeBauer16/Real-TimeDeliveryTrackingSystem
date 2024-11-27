package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String email;
    private String token;

}
