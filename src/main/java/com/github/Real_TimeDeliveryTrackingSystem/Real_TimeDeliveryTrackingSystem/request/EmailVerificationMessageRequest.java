package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationMessageRequest implements Serializable {

    private String email;
    public String code;
}
