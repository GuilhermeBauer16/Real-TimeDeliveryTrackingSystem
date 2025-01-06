package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCodeRequest {

    private String email;
    private String code;
}
