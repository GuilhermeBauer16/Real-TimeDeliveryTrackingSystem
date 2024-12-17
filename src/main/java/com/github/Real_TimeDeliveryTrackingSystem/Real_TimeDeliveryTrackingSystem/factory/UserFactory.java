package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;

import java.util.UUID;

public class UserFactory {
    public UserFactory() {
    }

    public static UserEntity create(String name, String email, String password, UserProfile userProfile) {
        return new UserEntity(UUID.randomUUID().toString(), name, email, password, userProfile);
    }
}
