package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

    String itemId;
    String itemTitle;
    BigDecimal itemPrice;
    String payerName;
    String payerSurname;
    String payerEmail;
    String payerCPF;
    String payerZipCode;
    String payerStreetName;
    String payerStreetNumber;
    String successUrl;
    String failureUrl;
    String pendingUrl;
    String notificationUrl;
}
