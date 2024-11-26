package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum UserProfile {
    @JsonProperty("customer")
    ROLE_CUSTOMER("CUSTOMER"),
    @JsonProperty("driver")
    ROLE_DRIVER("DRIVER"),
    @JsonProperty("admin")
    ROLE_ADMIN("ADMIN");

    private final String profile;

    UserProfile(String userProfile) {
        this.profile = userProfile;
    }


    private static final String UNKNOWN_USER_MESSAGE = "Unknown user profile: ";

    public static UserProfile fromString(String userProfile) {
        for (UserProfile profile : UserProfile.values()) {
            if (profile.profile.equalsIgnoreCase(userProfile)) {
                return profile;
            }
        }
        throw new IllegalArgumentException(UNKNOWN_USER_MESSAGE + userProfile);
    }
}
