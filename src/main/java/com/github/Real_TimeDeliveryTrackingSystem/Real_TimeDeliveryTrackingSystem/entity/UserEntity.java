package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;

    @Column(name = "user_profile")
    @Enumerated(EnumType.STRING)
    private UserProfile userProfile;

}
