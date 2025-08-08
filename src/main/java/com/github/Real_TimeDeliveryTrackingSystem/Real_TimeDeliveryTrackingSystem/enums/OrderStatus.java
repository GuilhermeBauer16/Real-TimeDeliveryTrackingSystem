package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {


    @JsonProperty("payment_approved")
    PAYMENT_APPROVED("PAYMENT_APPROVED"),

    @JsonProperty("payment_rejected")
    PAYMENT_REJECTED("PAYMENT_REJECTED"),

    @JsonProperty("canceled")
    CANCELED("CANCELED"),

    @JsonProperty("delivering")
    DELIVERING("DELIVERING"),

    @JsonProperty("delivered")
    DELIVERED("DELIVERED");

    private String orderStatus;

    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
