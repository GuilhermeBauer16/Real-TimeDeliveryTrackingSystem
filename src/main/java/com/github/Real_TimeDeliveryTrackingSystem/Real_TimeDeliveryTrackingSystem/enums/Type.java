package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Type {
    @JsonProperty("car")
    CAR("CAR"),
    @JsonProperty("truck")
    TRUCK("TRUCK"),
    @JsonProperty("van")
    VAN("VAN"),
    @JsonProperty("motorcycle")
    MOTORCYCLE("MOTORCYCLE");

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
