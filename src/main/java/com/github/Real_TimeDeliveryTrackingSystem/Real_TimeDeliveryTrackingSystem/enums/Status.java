package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {

    @JsonProperty("available")
    AVAILABLE("available"),
    @JsonProperty("in_use")
    IN_USE("IN_USE"),
    @JsonProperty("maintenance")
    MAINTENANCE("MAINTENANCE"),
    @JsonProperty("out_of_service")
    OUT_OF_SERVICE("OUT_OF_SERVICE");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

