package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private BigDecimal transactionAmount;
    private String token;
    private String description;
    private Integer installments;
    private String paymentMethodId;
    private String payerEmail;
}
